package com.example.tickets.ticket;

import com.example.tickets.httpclient.DefaultHttpClient;
import com.example.tickets.request.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    DefaultHttpClient<LatestResponse> httpClient;

    @Override
    public List<Ticket> getLatest(LatestRequest request) throws TicketServiceException {
        String stringRequest = request.toString();
        log.info("Sent request: " + stringRequest);
        LatestResponse response = httpClient.get(stringRequest, LatestResponse.class);
        log.info("Got response: " + response);
        if (!response.getSuccess()) {
            throw new TicketServiceException(response.getError());
        }
        return response.getData();
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
