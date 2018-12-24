package com.example.tickets.service;

import com.example.tickets.exception.TicketServiceException;
import com.example.tickets.service.request.*;
import com.example.tickets.ticket.TicketJson;

import java.util.List;

public interface TicketService {

    List<TicketJson> getLatest(LatestRequest request) throws TicketServiceException;

    List<TicketJson> getMonthMatrix(MonthMatrixRequest request) throws TicketServiceException;

    List<TicketJson> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) throws TicketServiceException;

    List<TicketJson> getCheap(CheapRequest request) throws TicketServiceException;

    List<TicketJson> getDirect(DirectRequest request) throws TicketServiceException;

    List<TicketJson> getCalendar(CalendarRequest request) throws TicketServiceException;

    List<TicketJson> getWeekMatrix(WeekMatrixRequest request) throws TicketServiceException;

}
