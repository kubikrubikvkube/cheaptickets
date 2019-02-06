package com.example.tickets.iata;

import com.example.tickets.resources.JsonResource;
import com.example.tickets.resources.ResourceResolver;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class IATAServiceImpl implements IATAService {
    private final Logger log = LoggerFactory.getLogger(IATAServiceImpl.class);
    private final IATARepository repository;
    private final ExampleMatcher exampleMatcher;
    private final IATAResolver resolver;
    private final IATADTOMapper mapper;
    private final ResourceResolver resourceResolver;

    public IATAServiceImpl(IATARepository repository, IATAResolver resolver, IATADTOMapper mapper, ResourceResolver resourceResolver) {
        this.repository = repository;
        this.resolver = resolver;
        this.mapper = mapper;
        this.resourceResolver = resourceResolver;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp").withIncludeNullValues();
    }


    @Override
    public IATA fromPlaceName(String place) {
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
        return repository.findByPlace(normalizedPlace);
    }

    @Override
    public IATA fromCode(String code) {
        String normalizedCode = code.toUpperCase();
        boolean exists = repository.existsByCode(code);

        if (!exists) {
            JsonNode iataNode = resourceResolver.resolve(JsonResource.CASES);
            String placeName = iataNode.get(normalizedCode).get("name").textValue();
            IATADTO iatadto = new IATADTO();
            iatadto.setPlace(placeName);
            iatadto.setCode(code);
            iatadto.setCanonical(true);
            IATA iata = mapper.fromDTO(iatadto);
            repository.save(iata);
        }
        return repository.findByCodeCanonical(normalizedCode);
    }


    @Override
    public IATA save(IATADTO dto) {
        IATA iata = mapper.fromDTO(dto);
        return repository.save(iata);
    }
}
