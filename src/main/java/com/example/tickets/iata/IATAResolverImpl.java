package com.example.tickets.iata;

import com.example.tickets.subscription.Subscription;
import com.example.tickets.subscription.SubscriptionDTO;
import com.example.tickets.util.DefaultHttpClient;
import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class IATAResolverImpl implements IATAResolver {
    private final DefaultHttpClient defaultHttpClient;
    private final int WAIT_TIMEOUTS = 5;

    public IATAResolverImpl(DefaultHttpClient defaultHttpClient) {
        this.defaultHttpClient = defaultHttpClient;
    }

    @Override
    public String resolve(String place) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://places.aviasales.ru/v2/places.json?")
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
            response = (JsonNode) responseFuture.get(WAIT_TIMEOUTS, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ServiceException(e);
        }
        JsonNode cityNode = response.get(0);
        JsonNode cityCode = cityNode.get("code");
        return cityCode.textValue();
    }

    @Override
    public SubscriptionDTO resolve(SubscriptionDTO dto) {
        var origin = resolve(dto.getOriginName());
        var destination = resolve(dto.getDestinationName());
        dto.setOrigin(origin);
        dto.setDestination(destination);
        return dto;
    }

    @Override
    public Subscription resolve(Subscription s) {
        var origin = resolve(s.getOriginName());
        var destination = resolve(s.getDestination());
        s.setOrigin(origin);
        s.setDestination(destination);
        return s;
    }
}