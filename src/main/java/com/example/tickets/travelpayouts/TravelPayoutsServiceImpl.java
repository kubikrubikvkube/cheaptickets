package com.example.tickets.travelpayouts;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.ticket.TicketDTO;
import com.example.tickets.travelpayouts.request.*;
import com.example.tickets.travelpayouts.response.LatestResponse;
import com.example.tickets.util.DefaultHttpClient;
import com.example.tickets.util.ServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.tickets.util.DateConverter.toDate;


@Service
public class TravelPayoutsServiceImpl implements TravelPayoutsService {
    private final Logger log = LoggerFactory.getLogger(TravelPayoutsServiceImpl.class);
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
    public List<Ticket> getDirect(DirectRequest request) {

        var stringRequest = request.toString();
        log.info("Sent request: " + stringRequest);
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
                ticketDTO.setDepart_date(toDate(request.getDepart_date()));
                ticketDTO.setReturn_date(toDate(request.getReturn_date()));
                ticketDTO.setValue(Integer.valueOf(price));
                ticketDTO.setAirline(airLine);
                ticketDTO.setFlightNumber(flightNumber);
                var departureDateTime = OffsetDateTime.from(DatatypeConverter.parseDateTime(departureAt).toInstant());
                var returnDateTime = OffsetDateTime.from(DatatypeConverter.parseDateTime(returnAt).toInstant());
                var expiresDateTime = OffsetDateTime.from(DatatypeConverter.parseDateTime(expiresAt).toInstant());
                ticketDTO.setDepart_date(departureDateTime);
                ticketDTO.setReturn_date(returnDateTime);
                ticketDTO.setExpiresAt(expiresDateTime);
                Ticket ticket = mapper.map(ticketDTO, Ticket.class);
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
