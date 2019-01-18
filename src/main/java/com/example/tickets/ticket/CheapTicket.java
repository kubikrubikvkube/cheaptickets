package com.example.tickets.ticket;

import com.example.tickets.notification.TicketNotification;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@Table(name = "ticket_cheap", indexes = {@Index(name = "idx_cheap_ticket", columnList = "id,origin,destination,departDate,numberOfChanges,value,actual,isExpired")})
public class CheapTicket extends Ticket {

    @CollectionTable(name = "ticket_cheap_sent_notifications")
    @OneToMany(targetEntity = TicketNotification.class, cascade = CascadeType.ALL)
    private List<TicketNotification> sentNotifications;
}
