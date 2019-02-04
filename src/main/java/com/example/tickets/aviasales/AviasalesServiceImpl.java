package com.example.tickets.aviasales;


import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.ticket.TicketDTOMapper;
import com.example.tickets.util.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Service
public class AviasalesServiceImpl implements AviasalesService {
    private final Logger log = LoggerFactory.getLogger(AviasalesServiceImpl.class);
    private final DefaultHttpClient<AviasalesResponse> defaultHttpClient;
    private final TicketDTOMapper mapper;
    private final int WAIT_TIMEOUTS = 5;

    public AviasalesServiceImpl(DefaultHttpClient<AviasalesResponse> defaultHttpClient, TicketDTOMapper mapper) {
        this.defaultHttpClient = defaultHttpClient;
        this.mapper = mapper;
    }

    @Override
    public List<Ticket> getOneWayTicket(String originIAT, String destinationIAT, LocalDate date, int range) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://lyssa.aviasales.ru/price_matrix?");
        sb.append("origin_iata=").append(originIAT).append("&");
        sb.append("destination_iata=").append(destinationIAT).append("&");
        sb.append("depart_start=").append(date).append("&");
        sb.append("depart_range=").append(range).append("&");
        sb.append("affiliate=true");
        var request = sb.toString();
        log.trace("Aviasales one-way ticket request: {}", request);
        AviasalesResponse response;
        try {
            CompletableFuture<AviasalesResponse> responseFuture = defaultHttpClient.getWithoutHeaders(request, AviasalesResponse.class);
            response = responseFuture.get(WAIT_TIMEOUTS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.debug(e.toString());
            return Collections.emptyList();
        }
        log.trace("Aviasales one-way ticket response: {}", response);
        List<TicketDTO> tickerPrices = response.getPrices();
        log.trace("Aviasales one-way ticket response size: {}", tickerPrices.size());
        tickerPrices.forEach(rawTicket -> {
            rawTicket.setOrigin(originIAT);
            rawTicket.setDestination(destinationIAT);
            rawTicket.setDepart_date(date);

        });
        return tickerPrices
                .stream()
                .map(mapper::fromDTO)
                .collect(Collectors.toList());


    }
}
