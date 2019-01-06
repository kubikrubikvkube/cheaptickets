package com.example.tickets.aviasales;

import com.example.tickets.aviasales.response.AviasalesResponse;
import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.util.DefaultHttpClient;
import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AviasalesServiceImpl implements AviasalesService {
    private final Logger log = LoggerFactory.getLogger(AviasalesServiceImpl.class);
    @Autowired
    private DefaultHttpClient<AviasalesResponse> defaultHttpClient;

    @Autowired
    private ModelMapper mapper;

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
        log.info("Aviasales one-way ticket request: " + request);
        AviasalesResponse response = defaultHttpClient.getWithoutHeaders(request, AviasalesResponse.class);
        log.info("Aviasales one-way ticket response: " + response);
        List<TicketDTO> tickerPrices = response.getPrices();
        log.info("Aviasales one-way ticket response size: " + tickerPrices.size());
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
    public List<Ticket> getReturnTicket(String originIAT, String destinationIAT, LocalDate departure, LocalDate returnDate, int departRange, int returnRange) throws ServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append("https://lyssa.aviasales.ru/price_matrix?");
        sb.append("origin_iata=").append(originIAT).append("&");
        sb.append("destination_iata=").append(destinationIAT).append("&");
        sb.append("depart_start=").append(departure).append("&");
        sb.append("return_start=").append(returnDate).append("&");
        sb.append("depart_range=").append(departRange).append("&");
        sb.append("return_range=").append(returnRange).append("&");
        sb.append("affiliate=false");
        var request = sb.toString();
        log.info("Aviasales return ticiket request: " + request);
        AviasalesResponse response = defaultHttpClient.getWithoutHeaders(request, AviasalesResponse.class);
        log.info("Aviasales return ticiket response: " + response);
        List<TicketDTO> tickerPrices = response.getPrices();
        log.info("Aviasales return ticket response size: " + tickerPrices.size());
        tickerPrices.forEach(rawTicket -> {
            rawTicket.setOrigin(originIAT);
            rawTicket.setDestination(destinationIAT);
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
        ObjectReader reader2 = new ObjectMapper().readerFor(new TypeReference<List<TicketDTO>>() {
        });
        List<TicketDTO> allNodes;
        try {
            allNodes = reader2.readValue(response);
        } catch (IOException ioe) {
            throw new ServiceException(ioe);
        }

        return allNodes
                .stream()
                .map(dto -> mapper.map(dto, Ticket.class))
                .collect(Collectors.toList());
    }
}
