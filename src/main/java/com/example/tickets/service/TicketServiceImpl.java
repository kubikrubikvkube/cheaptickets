package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.httpclient.DefaultHttpClient;
import com.example.tickets.service.travelpayouts.request.*;
import com.example.tickets.service.travelpayouts.response.LatestResponse;
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
    public List<TicketJson> getLatest(LatestRequest request) throws ServiceException {
        String stringRequest = request.toString();
        log.info("Sent request: " + stringRequest);
        LatestResponse response = httpClient.getWithHeaders(stringRequest, LatestResponse.class);
        log.info("Got response: " + response);
        if (!response.getSuccess()) {
            throw new ServiceException(response.getError());
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
