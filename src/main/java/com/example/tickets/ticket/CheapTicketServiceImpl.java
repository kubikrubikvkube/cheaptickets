package com.example.tickets.ticket;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CheapTicketServiceImpl implements CheapTicketService {
    private final CheapTicketRepository repository;

    public CheapTicketServiceImpl(CheapTicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(CheapTicket cheapTicket) {
        repository.save(cheapTicket);
    }

    @Override
    public void saveAll(Iterable<CheapTicket> cheapTickets) {
        repository.saveAll(cheapTickets);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
