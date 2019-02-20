package com.example.tickets.aviasales;


import com.example.tickets.ticket.TicketDto;
import com.example.tickets.ticket.TicketDtoMapper;
import com.example.tickets.util.DefaultHttpClient;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Service
public class AviasalesServiceImpl implements AviasalesService {
    private static final Logger log = LoggerFactory.getLogger(AviasalesServiceImpl.class);
    private static final int WAIT_TIMEOUT_IN_SECONDS = 5;
    private final DefaultHttpClient<AviasalesResponse> defaultHttpClient;
    private final TicketDtoMapper mapper;

    public AviasalesServiceImpl(DefaultHttpClient<AviasalesResponse> defaultHttpClient, TicketDtoMapper mapper) {
        this.defaultHttpClient = defaultHttpClient;
        this.mapper = mapper;
    }

    @Override
    public List<TicketDto> getOneWayTicket(@NonNull String originIAT, @NonNull String destinationIAT, @NonNull LocalDate date, @NonNull Integer range) {
        UriComponentsBuilder queryBuilder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("lyssa.aviasales.ru")
                .path("/price_matrix");

        queryBuilder.queryParam("origin_iata", originIAT);
        queryBuilder.queryParam("destination_iata", destinationIAT);
        queryBuilder.queryParam("depart_start", date);
        queryBuilder.queryParam("depart_range", range);
        queryBuilder.queryParam("affiliate", true);

        var request = queryBuilder.build(true).toUriString();

        log.trace("Aviasales one-way ticket request: {}", request);
        AviasalesResponse response;
        try {
            CompletableFuture<AviasalesResponse> responseFuture = defaultHttpClient.getWithoutHeaders(request, AviasalesResponse.class);
            response = responseFuture.get(WAIT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.debug(e.toString());
            return Collections.emptyList();
        }
        log.trace("Aviasales one-way ticket response: {}", response);
        List<TicketDto> tickerPrices = response.getPrices();
        log.trace("Aviasales one-way ticket response size: {}", tickerPrices.size());
        tickerPrices.forEach(ticketDto -> {
            ticketDto.setOrigin(originIAT);
            ticketDto.setDestination(destinationIAT);
            ticketDto.setDepartDate(date);

        });
        return tickerPrices;
    }
}
