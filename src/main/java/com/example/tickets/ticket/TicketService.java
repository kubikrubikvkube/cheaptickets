package com.example.tickets.ticket;

import com.example.tickets.request.*;

import java.util.List;

public interface TicketService {

    List<Ticket> getLatest(LatestRequest request) throws TicketServiceException;

    List<Ticket> getMonthMatrix(MonthMatrixRequest request) throws TicketServiceException;

    List<Ticket> getNearestPlacesMatrix(NearestPlacesMatrixRequest request) throws TicketServiceException;

    List<Ticket> getCheap(CheapRequest request) throws TicketServiceException;

    List<Ticket> getDirect(DirectRequest request) throws TicketServiceException;

    List<Ticket> getCalendar(CalendarRequest request) throws TicketServiceException;

    List<Ticket> getWeekMatrix(WeekMatrixRequest request) throws TicketServiceException;

}
