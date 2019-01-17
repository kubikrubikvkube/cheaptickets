package com.example.tickets.ticket;

public interface CheapTicketService {

    void save(CheapTicket cheapTicket);

    void saveAll(Iterable<CheapTicket> cheapTickets);

    long count();
}
