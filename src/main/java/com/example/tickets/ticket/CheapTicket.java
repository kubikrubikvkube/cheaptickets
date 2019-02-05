package com.example.tickets.ticket;

import com.example.tickets.notification.RouteNotification;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "ticket_cheap", indexes = {@Index(name = "idx_cheap_ticket", columnList = "id,origin,destination,departDate,numberOfChanges,value,actual,isExpired")})
public class CheapTicket extends Ticket {

    @CollectionTable(name = "ticket_cheap_sent_notifications")
    @OneToMany(targetEntity = RouteNotification.class, cascade = CascadeType.ALL)
    private List<RouteNotification> sentNotifications;
}
