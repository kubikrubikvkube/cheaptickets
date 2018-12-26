package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.repository.Ticket;
import com.example.tickets.service.travelpayouts.request.*;

import java.util.List;

/**
 * TravelPayouts API
 */
public interface TicketService {

    List<Ticket> getLatest(LatestRequest request) throws ServiceException;

    List<Ticket> getMonthMatrix(MonthMatrixRequest request) throws ServiceException;

    List<Ticket> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) throws ServiceException;

    List<Ticket> getCheap(CheapRequest request) throws ServiceException;

    List<Ticket> getDirect(DirectRequest request) throws ServiceException;

    List<Ticket> getCalendar(CalendarRequest request) throws ServiceException;

    List<Ticket> getWeekMatrix(WeekMatrixRequest request) throws ServiceException;

}
