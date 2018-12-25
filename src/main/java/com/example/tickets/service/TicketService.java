package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.service.travelpayouts.request.*;

import java.util.List;

/**
 * TravelPayouts API
 */
public interface TicketService {

    List<TicketJson> getLatest(LatestRequest request) throws ServiceException;

    List<TicketJson> getMonthMatrix(MonthMatrixRequest request) throws ServiceException;

    List<TicketJson> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) throws ServiceException;

    List<TicketJson> getCheap(CheapRequest request) throws ServiceException;

    List<TicketJson> getDirect(DirectRequest request) throws ServiceException;

    List<TicketJson> getCalendar(CalendarRequest request) throws ServiceException;

    List<TicketJson> getWeekMatrix(WeekMatrixRequest request) throws ServiceException;

}
