package com.example.tickets.statistics;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ticket_statistics_routes", indexes = {@Index(name = "idx_ticket_statistics_routes", columnList = "id,origin,destination")})
public class TicketStatistics {
    @Id
    @GeneratedValue
    private Long id;

    private String origin;

    private String destination;

    @CollectionTable(name = "ticket_statistics_by_day_collection")
    @OneToMany(targetEntity = TicketStatisticsByDay.class, cascade = CascadeType.ALL)
    private List<TicketStatisticsByDay> ticketStatisticsByDay;

    @CollectionTable(name = "ticket_statistics_by_month_collection")
    @OneToMany(targetEntity = TicketStatisticsByMonth.class, cascade = CascadeType.ALL)
    private List<TicketStatisticsByMonth> ticketStatisticsByMonth;
}
