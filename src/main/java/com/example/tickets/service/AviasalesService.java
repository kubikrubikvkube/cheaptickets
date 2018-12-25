package com.example.tickets.service;

import com.example.tickets.exception.ServiceException;
import com.example.tickets.ticket.TicketJson;

import java.time.LocalDate;
import java.util.List;

public interface AviasalesService {
    List<TicketJson> getOneWayTicket(String originIAT, String destinationIAT, LocalDate date, int range) throws ServiceException;

    List<TicketJson> getReturnTicket(String originIAT, String destinationIAT, LocalDate departure, LocalDate returnDate, int range) throws ServiceException;
}
