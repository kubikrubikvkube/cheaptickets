package com.example.tickets.travelpayouts;

import com.example.tickets.ticket.TicketDto;
import com.example.tickets.ticket.TicketDtoMapper;
import com.example.tickets.travelpayouts.request.*;
import com.example.tickets.travelpayouts.response.LatestResponse;
import com.example.tickets.util.DefaultHttpClient;
import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Service
public class TravelPayoutsServiceImpl implements TravelPayoutsService {
    private static final Logger log = LoggerFactory.getLogger(TravelPayoutsServiceImpl.class);
    private final DefaultHttpClient<LatestResponse> httpClient;
    private final TicketDtoMapper mapper;
    private final int WAIT_TIMEOUT = 5;

    public TravelPayoutsServiceImpl(DefaultHttpClient<LatestResponse> httpClient, TicketDtoMapper mapper) {
        this.httpClient = httpClient;
        this.mapper = mapper;
    }

    @Override
    public List<TicketDto> getLatest(LatestRequest request) {
        String stringRequest = request.toString();
        log.trace("Sent request: {}", stringRequest);
        LatestResponse response;
        try {
            CompletableFuture<LatestResponse> responseOptional = httpClient.getWithHeaders(stringRequest, LatestResponse.class);
            response = responseOptional.get(WAIT_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ServiceException(e);
        }
        log.trace("Got response: {}", response);

        return response.getData();


    }

    @Override
    public List<TicketDto> getMonthMatrix(MonthMatrixRequest request) {
        return Collections.emptyList();
    }

    @Override
    public List<TicketDto> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) {
        return Collections.emptyList();
    }

    @Override
    public List<TicketDto> getCheap(CheapRequest request) {
        return Collections.emptyList();
    }

    @Override
    public List<TicketDto> getDirect(DirectRequest request) {

        String stringRequest = request.toString();
        log.trace("Sent request: {}", stringRequest);
        JsonNode response;
        try {
            CompletableFuture<JsonNode> responseFuture = httpClient.getJsonResponseWithHeaders(stringRequest);
            response = responseFuture.get(WAIT_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.debug(e.toString());
            return Collections.emptyList();
        }

        List<JsonNode> values = response.findValues(request.getDestination());
        List<TicketDto> tickets = new ArrayList<>();
        values.forEach(rootNode -> {
            for (JsonNode rawTicket : rootNode) {
                TicketDto ticketDto = new TicketDto();
                String price = rawTicket.get("price").toString();
                String airLine = rawTicket.get("airline").toString();
                String flightNumber = rawTicket.get("flight_number").toString();
                String departureAt = rawTicket.get("departure_at").toString().replaceAll("\"", "");
                String returnAt = rawTicket.get("return_at").toString().replaceAll("\"", "");
                String expiresAt = rawTicket.get("expires_at").textValue().replaceAll("\"", "");
                ticketDto.setOrigin(request.getOrigin());
                ticketDto.setDestination(request.getDestination());
                ticketDto.setDepartDate(request.getDepartDate());
                ticketDto.setReturnDate(request.getReturnDate());
                ticketDto.setValue(Integer.valueOf(price));
                ticketDto.setAirline(airLine);
                ticketDto.setFlightNumber(flightNumber);
                var departureDateTime = LocalDate.from(Instant.parse(departureAt).atZone(ZoneId.systemDefault()));
                var returnDateTime = LocalDate.from(Instant.parse(returnAt).atZone(ZoneId.systemDefault()));
                var expiresDateTime = LocalDateTime.from(Instant.parse(expiresAt).atZone(ZoneId.systemDefault()));
                ticketDto.setDepartDate(departureDateTime);
                ticketDto.setReturnDate(returnDateTime);
                ticketDto.setExpiresAt(expiresDateTime);
                tickets.add(ticketDto);
            }

        });
        return tickets;
    }

    @Override
    public List<TicketDto> getCalendar(CalendarRequest request) {
        return Collections.emptyList();
    }

    @Override
    public List<TicketDto> getWeekMatrix(WeekMatrixRequest request) {
        return Collections.emptyList();
    }
}
