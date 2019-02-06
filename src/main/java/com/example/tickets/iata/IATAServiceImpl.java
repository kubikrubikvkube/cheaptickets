package com.example.tickets.iata;

import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class IATAServiceImpl implements IATAService {
    private final Logger log = LoggerFactory.getLogger(IATAServiceImpl.class);
    private final IATARepository repository;
    private final ExampleMatcher exampleMatcher;
    private final IATAResolver resolver;
    private final IATADTOMapper mapper;

    public IATAServiceImpl(IATARepository repository, IATAResolver resolver, IATADTOMapper mapper) {
        this.repository = repository;
        this.resolver = resolver;
        this.mapper = mapper;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp").withIncludeNullValues();
    }


    @Override
    public Optional<IATA> fromPlaceName(String place) {
        String normalizedPlace = place.toLowerCase();
        boolean exists = repository.existsByPlace(normalizedPlace);
        if (!exists) {
            String code = resolver.resolve(normalizedPlace);
            IATADTO dto = new IATADTO();
            dto.setCode(code);
            dto.setPlace(normalizedPlace);
            dto.setCanonical(false);
            IATA iata = mapper.fromDTO(dto);
            repository.save(iata);
        }
        var iataOptional = repository.findByPlace(normalizedPlace);
        if (iataOptional.isEmpty()) {
            throw new ServiceException(String.format("IATA code for place is not saved %s", place));
        }
        return iataOptional;
    }

    @Override
    public Optional<IATA> fromCode(String code) {
        String normalizedCode = code.toUpperCase();
        boolean exists = repository.existsByCode(code);
        IATADTO dto;
        if (!exists) {
            try {
                Resource resource = new ClassPathResource("travelpayouts/cases.json");
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode objectTree = objectMapper.readTree(resource.getURL());
                JsonNode iataNode = objectTree.get(code);
                String placeName = iataNode.get("name").textValue();
                IATADTO iatadto = new IATADTO();
                iatadto.setPlace(placeName);
                iatadto.setCode(code);
                iatadto.setCanonical(true);
                IATA iata = mapper.fromDTO(iatadto);
                repository.save(iata);
            } catch (IOException e) {
                throw new ServiceException(e);
            }
        }
        Optional<IATA> iataOptional = repository.findByCodeCanonical(normalizedCode);
        if (iataOptional.isEmpty()) {
            throw new ServiceException(String.format("IATA place for code is not saved %s", code));
        }
        return iataOptional;
    }


    @Override
    public IATA save(IATADTO dto) {
        IATA iata = mapper.fromDTO(dto);
        return repository.save(iata);
    }
}
