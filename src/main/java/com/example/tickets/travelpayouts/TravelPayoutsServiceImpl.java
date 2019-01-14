package com.example.tickets.travelpayouts;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.ticket.TicketDTOMapper;
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
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TravelPayoutsServiceImpl implements TravelPayoutsService {
    private final Logger log = LoggerFactory.getLogger(TravelPayoutsServiceImpl.class);
    private final DefaultHttpClient httpClient;
    private final TicketDTOMapper mapper = TicketDTOMapper.INSTANCE;

    public TravelPayoutsServiceImpl(DefaultHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<Ticket> getLatest(LatestRequest request) throws ServiceException {
        String stringRequest = request.toString();
        log.trace("Sent request: " + stringRequest);
        LatestResponse response = (LatestResponse) httpClient.getWithHeaders(stringRequest, LatestResponse.class);
        log.trace("Got response: " + response);
        if (!response.getSuccess()) {
            throw new ServiceException(response.getError());
        }
        List<TicketDTO> ticketDTOS = response.getData();
        return ticketDTOS
                .stream()
                .map(mapper::dtoToTicket)
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
    public List<Ticket> getDirect(DirectRequest request) {

        String stringRequest = request.toString();
        log.trace("Sent request: " + stringRequest);
        JsonNode response = httpClient.getJsonResponseWithHeaders(stringRequest);

        List<JsonNode> values = response.findValues(request.getDestination());
        List<Ticket> tickets = new ArrayList<>();
        values.forEach(rootNode -> {
            for (JsonNode rawTicket : rootNode) {
                TicketDTO ticketDTO = new TicketDTO();
                String price = rawTicket.get("price").toString();
                String airLine = rawTicket.get("airline").toString();
                String flightNumber = rawTicket.get("flight_number").toString();
                String departureAt = rawTicket.get("departure_at").toString().replaceAll("\"", "");
                String returnAt = rawTicket.get("return_at").toString().replaceAll("\"", "");
                String expiresAt = rawTicket.get("expires_at").textValue().replaceAll("\"", "");
                ticketDTO.setOrigin(request.getOrigin());
                ticketDTO.setDestination(request.getDestination());
                ticketDTO.setDepart_date(request.getDepart_date());
                ticketDTO.setReturn_date(request.getReturn_date());
                ticketDTO.setValue(Integer.valueOf(price));
                ticketDTO.setAirline(airLine);
                ticketDTO.setFlightNumber(flightNumber);
                var departureDateTime = LocalDate.from(Instant.parse(departureAt).atZone(ZoneId.systemDefault()));
                var returnDateTime = LocalDate.from(Instant.parse(returnAt).atZone(ZoneId.systemDefault()));
                var expiresDateTime = LocalDateTime.from(Instant.parse(expiresAt).atZone(ZoneId.systemDefault()));
                ticketDTO.setDepart_date(departureDateTime);
                ticketDTO.setReturn_date(returnDateTime);
                ticketDTO.setExpiresAt(expiresDateTime);
                Ticket ticket = mapper.dtoToTicket(ticketDTO);
                tickets.add(ticket);
            }

        });
        return tickets;
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
