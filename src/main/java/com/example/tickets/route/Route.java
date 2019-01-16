package com.example.tickets.route;

import com.example.tickets.notification.TicketNotification;
import com.example.tickets.ticket.Ticket;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_route", columnList = "id,origin,destination,sumValue,tripDurationInDays")})
public class Route {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Date creationTimestamp;

    /**
     * Место отправления
     */
    private String origin;

    /**
     * Пункт назначения
     */
    private String destination;

    /**
     * Билет из места отправления
     */
    @OneToOne
    private Ticket departTicket;

    /**
     * Обратный билет
     */
    @OneToOne
    private Ticket returnTicket;
    /**
     * Общая стоимость поездки
     */
    private Integer sumValue;

    /**
     * Время поездки в днях
     */
    private Integer tripDurationInDays;

    @OneToMany
    @CollectionTable(name = "route_notifications")
    @JsonBackReference
    private List<TicketNotification> ticketNotifications;
}
