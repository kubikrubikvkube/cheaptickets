package com.example.tickets.aviasales;

import com.example.tickets.aviasales.response.AviasalesResponse;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.util.DefaultHttpClient;
import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AviasalesServiceImpl implements AviasalesService {
    private final Logger log = LoggerFactory.getLogger(AviasalesServiceImpl.class);
    private final DefaultHttpClient<AviasalesResponse> defaultHttpClient;
    private final ModelMapper mapper;

    public AviasalesServiceImpl(DefaultHttpClient<AviasalesResponse> defaultHttpClient, ModelMapper mapper) {
        this.defaultHttpClient = defaultHttpClient;
        this.mapper = mapper;
    }

    @Override
    public List<Ticket> getOneWayTicket(String originIAT, String destinationIAT, LocalDate date, int range) throws ServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://lyssa.aviasales.ru/price_matrix?");
        sb.append("origin_iata=").append(originIAT).append("&");
        sb.append("destination_iata=").append(destinationIAT).append("&");
        sb.append("depart_start=").append(date).append("&");
        sb.append("depart_range=").append(range).append("&");
        sb.append("affiliate=false");
        var request = sb.toString();
        log.trace("Aviasales one-way ticket request: " + request);
        AviasalesResponse response = defaultHttpClient.getWithoutHeaders(request, AviasalesResponse.class);
        log.trace("Aviasales one-way ticket response: " + response);
        List<TicketDTO> tickerPrices = response.getPrices();
        log.trace("Aviasales one-way ticket response size: " + tickerPrices.size());
        tickerPrices.forEach(rawTicket -> {
            rawTicket.setOrigin(originIAT);
            rawTicket.setDestination(destinationIAT);
            rawTicket.setDepart_date(date);

        });
        return tickerPrices
                .stream()
                .map(dto -> mapper.map(dto, Ticket.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getReturnTicket(String originIATA, String destinationIATA, LocalDate departure, LocalDate returnDate, int departRange, int returnRange) throws ServiceException {
        StringBuilder sb = new StringBuilder();

        sb.append("https://lyssa.aviasales.ru/price_matrix?");
        sb.append("origin_iata=").append(originIATA).append("&");
        sb.append("destination_iata=").append(destinationIATA).append("&");
        sb.append("depart_start=").append(departure).append("&");
        sb.append("return_start=").append(returnDate).append("&");
        sb.append("depart_range=").append(departRange).append("&");
        sb.append("return_range=").append(returnRange).append("&");
        sb.append("affiliate=false");
        var request = sb.toString();
        log.trace("Aviasales return ticiket request: " + request);
        AviasalesResponse response = defaultHttpClient.getWithoutHeaders(request, AviasalesResponse.class);
        log.trace("Aviasales return ticiket response: " + response);
        List<TicketDTO> tickerPrices = response.getPrices();
        log.trace("Aviasales return ticket response size: " + tickerPrices.size());
        tickerPrices.forEach(rawTicket -> {
            rawTicket.setOrigin(originIATA);
            rawTicket.setDestination(destinationIATA);
            rawTicket.setDepart_date(departure);
            rawTicket.setReturn_date(returnDate);

        });
        return tickerPrices
                .stream()
                .map(dto -> mapper.map(dto, Ticket.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getTicketsMap(String originIAT, LocalDate departDate, boolean onlyDirect) throws ServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://lyssa.aviasales.ru/map?");
        sb.append("origin_iata=").append(originIAT).append("&");
        sb.append("only_direct=").append(onlyDirect).append("&");
        sb.append("depart_dates[]=").append(departDate).append("&");
        sb.append("one_way=").append(true).append("&");
        sb.append("show_to_affiliates=false");
        var stringRequest = sb.toString();

        JsonNode response = defaultHttpClient.getJsonResponseWithoutHeaders(stringRequest);
        List<Ticket> tickets = new ArrayList<>();
        response.elements().forEachRemaining(jsonNode -> {
            TicketDTO dto = mapper.map(jsonNode, TicketDTO.class);
            Ticket ticket = mapper.map(dto, Ticket.class);
            tickets.add(ticket);
        });

        return tickets;

    }

    @Override
    public List<Ticket> getTicketsMap(String originIAT, int minTripDuration, int maxTripDuration) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://lyssa.aviasales.ru/map?");
        sb.append("origin_iata=").append(originIAT).append("&");
        sb.append("one_way=").append(false).append("&");
        sb.append("min_trip_duration=").append(minTripDuration).append("&");
        sb.append("max_trip_duration=").append(maxTripDuration).append("&");
        sb.append("show_to_affiliates=false");
        var stringRequest = sb.toString();

        JsonNode response = defaultHttpClient.getJsonResponseWithoutHeaders(stringRequest);
        List<Ticket> tickets = new ArrayList<>();
        try {
            while (response.elements().hasNext()) {
                JsonNode jsonNode = response.elements().next();
                TicketDTO dto = new ObjectMapper().readValue(jsonNode.toString(), TicketDTO.class);
                Ticket ticket = mapper.map(dto, Ticket.class);
                tickets.add(ticket);
            }
        } catch (IOException ioe) {
            throw new ServiceException(ioe);
        }
        return tickets;
    }
}
