package com.example.tickets.route;

import com.example.tickets.notification.TicketNotification;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_route", columnList = "origin,destination,departDate,returnDate,sumValue,tripDuration")})
public class Route {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Calendar creationTimestamp;

    /**
     * Место отправления
     */
    private String origin;

    /**
     * Пункт назначения
     */
    private String destination;

    /**
     * Дата отправления
     */
    private Date departDate;

    /**
     * Дата возвращения
     */
    private Date returnDate;

    /**
     * Стоимость билета туда
     */
    private Integer departTicketValue;

    /**
     * Стоимость обратного билета
     */
    private Integer returnTicketValue;
    /**
     * Общая стоимость поездки
     */
    private Integer sumValue;

    /**
     * Время поездки в днях
     */
    private Integer tripDuration;

    @OneToMany
    private List<TicketNotification> ticketNotifications;
}
