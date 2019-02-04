package com.example.tickets.iata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public String fromCode(String code) {
        List<IATA> byCode = repository.findByCode(code);
        return byCode.stream().findAny().get().getPlace(); //и будет возвращаться какое-то говно
    }

    @Override
    public String fromPlaceName(String place) {
        boolean exists = repository.existsByPlace(place);
        if (!exists) {
            String code = resolver.resolve(place);
            IATADTO dto = new IATADTO();
            dto.setCode(code);
            dto.setPlace(place);
            IATA iata = mapper.fromDTO(dto);
            repository.save(iata);
        }
        var iataOptional = repository.findByPlace(place);
        IATA iata = iataOptional.get();
        return iata.getCode();
    }
}
