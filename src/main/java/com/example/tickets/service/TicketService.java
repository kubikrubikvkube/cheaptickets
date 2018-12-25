package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.service.request.*;
import com.example.tickets.ticket.TicketJson;

import java.util.List;

public interface TicketService {

    List<TicketJson> getLatest(LatestRequest request) throws ServiceException;

    List<TicketJson> getMonthMatrix(MonthMatrixRequest request) throws ServiceException;

    List<TicketJson> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) throws ServiceException;

    List<TicketJson> getCheap(CheapRequest request) throws ServiceException;

    List<TicketJson> getDirect(DirectRequest request) throws ServiceException;

    List<TicketJson> getCalendar(CalendarRequest request) throws ServiceException;

    List<TicketJson> getWeekMatrix(WeekMatrixRequest request) throws ServiceException;

}
