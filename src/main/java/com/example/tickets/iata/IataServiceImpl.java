package com.example.tickets.iata;

import com.example.tickets.resources.JsonResource;
import com.example.tickets.resources.ResourceResolver;
import com.example.tickets.util.DefaultHttpClient;
import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class IataServiceImpl implements IataService {
    private static final Logger log = LoggerFactory.getLogger(IataServiceImpl.class);
    private final IataRepository repository;
    private final ExampleMatcher exampleMatcher;
    private final IataDtoMapper mapper;
    private final DefaultHttpClient defaultHttpClient;
    private final int WAIT_TIMEOUT_IN_SECONDS = 5;
    private final ResourceResolver resourceResolver;

    public IataServiceImpl(IataRepository repository, IataDtoMapper mapper, DefaultHttpClient defaultHttpClient, ResourceResolver resourceResolver) {
        this.repository = repository;
        this.mapper = mapper;
        this.defaultHttpClient = defaultHttpClient;
        this.resourceResolver = resourceResolver;
        this.exampleMatcher = ExampleMatcher.matchingAll().withIgnorePaths("id", "creationTimestamp").withIncludeNullValues();
    }

    @Override
    @Cacheable("iataServiceImpl_resolve")
    public String resolve(String place) {

        StringBuilder sb = new StringBuilder();
        sb.append("http://places.aviasales.ru/v2/places.json?")
                .append("locale=ru&")
                .append("max=1&")
                .append("term=").append(place).append("&")
                .append("types[]=city&")
                .append("types[]=airport&")
                .append("types[]=country");
        var request = sb.toString();

        JsonNode response;
        try {
            CompletableFuture responseFuture = defaultHttpClient.getJsonResponseWithoutHeaders(request);
            response = (JsonNode) responseFuture.get(WAIT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ServiceException(e);
        }
        JsonNode cityNode = response.get(0);
        JsonNode cityCode = cityNode.get("code");
        return cityCode.textValue();
    }

    @Override
    @Cacheable("iataServiceImpl_fromPlaceName")
    public Iata fromPlaceName(String place) {
        String normalizedPlace = place.toLowerCase();
        boolean exists = repository.existsByPlace(normalizedPlace);
        if (!exists) {
            String code = this.resolve(normalizedPlace);
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
    @Cacheable("iataServiceImpl_fromCode")
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
