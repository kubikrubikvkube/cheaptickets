package com.example.tickets.service;

import com.example.tickets.exception.TicketServiceException;
import com.example.tickets.httpclient.DefaultHttpClient;
import com.example.tickets.service.request.*;
import com.example.tickets.service.response.LatestResponse;
import com.example.tickets.ticket.TicketJson;
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
    public List<TicketJson> getLatest(LatestRequest request) throws TicketServiceException {
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
    public List<TicketJson> getMonthMatrix(MonthMatrixRequest request) {
        return null;
    }

    @Override
    public List<TicketJson> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) {
        return null;
    }

    @Override
    public List<TicketJson> getCheap(CheapRequest request) {
        return null;
    }

    @Override
    public List<TicketJson> getDirect(DirectRequest request) {
        return null;
    }

    @Override
    public List<TicketJson> getCalendar(CalendarRequest request) {
        return null;
    }

    @Override
    public List<TicketJson> getWeekMatrix(WeekMatrixRequest request) {
        return null;
    }
}
