package com.example.tickets.ticket;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "ticket_cheap", indexes = {@Index(name = "idx_cheap_ticket", columnList = "id,origin,destination,departDate,numberOfChanges,value,actual,isExpired")})
public class CheapTicket extends Ticket {
}
