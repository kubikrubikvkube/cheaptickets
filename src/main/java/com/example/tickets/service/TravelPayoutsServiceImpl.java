package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.httpclient.DefaultHttpClient;
import com.example.tickets.repository.Ticket;
import com.example.tickets.service.travelpayouts.request.*;
import com.example.tickets.service.travelpayouts.response.LatestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.tickets.util.DateConverter.toDate;

@Log
@Service
public class TravelPayoutsServiceImpl implements TravelPayoutsService {
    @Autowired
    private DefaultHttpClient httpClient;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<Ticket> getLatest(LatestRequest request) throws ServiceException {
        String stringRequest = request.toString();
        log.info("Sent request: " + stringRequest);
        LatestResponse response = (LatestResponse) httpClient.getWithHeaders(stringRequest, LatestResponse.class);
        log.info("Got response: " + response);
        if (!response.getSuccess()) {
            throw new ServiceException(response.getError());
        }
        List<TicketDTO> ticketDTOS = response.getData();
        return ticketDTOS
                .stream()
                .map(dto -> mapper.map(dto, Ticket.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getMonthMatrix(MonthMatrixRequest request) {
        return null;
    }

    @Override
    public List<Ticket> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) {
        return null;
    }

    @Override
    public List<Ticket> getCheap(CheapRequest request) {
        return null;
    }

    @Override
    public List<Ticket> getDirect(DirectRequest request) throws IOException {

        var stringRequest = request.toString();
        log.info("Sent request: " + stringRequest);
        JsonNode response = httpClient.getJsonResponseWithHeaders(stringRequest);

        List<JsonNode> values = response.findValues(request.getDestination());

        values.forEach(rootNode -> {

            Iterator<JsonNode> iterator = rootNode.iterator();
            while (iterator.hasNext()) {
                JsonNode rawTicket = iterator.next();
                TicketDTO ticketDTO = new TicketDTO();
                String price = rawTicket.get("price").toString();
                String airLine = rawTicket.get("airline").toString();
                String flightNumber = rawTicket.get("flight_number").toString();
                String departureAt = rawTicket.get("departure_at").toString().replaceAll("\"", "");
                String returnAt = rawTicket.get("return_at").toString().replaceAll("\"", "");
                String expiresAt = rawTicket.get("expires_at").textValue().replaceAll("\"", "");
                ticketDTO.setValue(Integer.valueOf(price));
                ticketDTO.setAirline(airLine);
                ticketDTO.setFlightNumber(flightNumber);
                ticketDTO.setDepart_date(toDate(departureAt));
                ticketDTO.setReturn_date(toDate(returnAt));
                ticketDTO.setExpiresAt(toDate(expiresAt));
                int h = 0;
            }

        });
        return null;
    }

    @Override
    public List<Ticket> getCalendar(CalendarRequest request) {
        return null;
    }

    @Override
    public List<Ticket> getWeekMatrix(WeekMatrixRequest request) {
        return null;
    }
}
