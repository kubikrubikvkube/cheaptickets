package com.example.tickets.travelpayouts;

import com.example.tickets.ticket.Ticket;
import com.example.tickets.travelpayouts.request.*;
import com.example.tickets.util.ServiceException;

import java.io.IOException;
import java.util.List;

/**
 * TravelPayouts API
 */
public interface TravelPayoutsService {

    List<Ticket> getLatest(LatestRequest request) throws ServiceException;

    List<Ticket> getMonthMatrix(MonthMatrixRequest request) throws ServiceException;

    List<Ticket> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) throws ServiceException;

    List<Ticket> getCheap(CheapRequest request) throws ServiceException;

    List<Ticket> getDirect(DirectRequest request) throws ServiceException, IOException;

    List<Ticket> getCalendar(CalendarRequest request) throws ServiceException;

    List<Ticket> getWeekMatrix(WeekMatrixRequest request) throws ServiceException;

}
