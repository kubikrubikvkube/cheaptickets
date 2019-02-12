package com.example.tickets.iata;

import com.example.tickets.resources.JsonResource;
import com.example.tickets.resources.ResourceResolver;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class IataServiceImpl implements IataService {
    private static final Logger log = LoggerFactory.getLogger(IataServiceImpl.class);
    private final IataRepository repository;
    private final ExampleMatcher exampleMatcher;
    private final IataResolver resolver;
    private final IataDtoMapper mapper;
    private final ResourceResolver resourceResolver;

    public IataServiceImpl(IataRepository repository, IataResolver resolver, IataDtoMapper mapper, ResourceResolver resourceResolver) {
        this.repository = repository;
        this.resolver = resolver;
        this.mapper = mapper;
        this.resourceResolver = resourceResolver;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp").withIncludeNullValues();
    }


    @Override
    public Iata fromPlaceName(String place) {
        String normalizedPlace = place.toLowerCase();
        boolean exists = repository.existsByPlace(normalizedPlace);
        if (!exists) {
            String code = resolver.resolve(normalizedPlace);
            IataDto dto = new IataDto();
            dto.setCode(code);
            dto.setPlace(normalizedPlace);
            dto.setCanonical(false);
            Iata iata = mapper.fromDto(dto);
            repository.save(iata);
        }
        return repository.findByPlace(normalizedPlace);
    }

    @Override
    public Iata fromCode(String code) {
        String normalizedCode = code.toUpperCase();
        boolean exists = repository.existsByCodeCanonical(code);

        if (!exists) {
            JsonNode iataNode = resourceResolver.resolve(JsonResource.CASES);
            String placeName = iataNode.get(normalizedCode).get("name").textValue();
            IataDto iatadto = new IataDto();
            iatadto.setPlace(placeName);
            iatadto.setCode(code);
            iatadto.setCanonical(true);
            Iata iata = mapper.fromDto(iatadto);
            repository.save(iata);
        }
        return repository.findByCodeCanonical(normalizedCode);
    }


    @Override
    public Iata save(IataDto dto) {
        Iata iata = mapper.fromDto(dto);
        return repository.save(iata);
    }
}
