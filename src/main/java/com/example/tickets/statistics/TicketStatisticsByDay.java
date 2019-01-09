package com.example.tickets.statistics;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class TicketStatisticsByDay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = TicketStatistics.class)
    @PrimaryKeyJoinColumn
    private Long ticketStatisticsId;

    private LocalDate date;

    private Long ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile5;
}
