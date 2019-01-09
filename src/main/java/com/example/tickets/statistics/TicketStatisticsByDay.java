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
    private Long subscriptionStatisticsId;

    private LocalDate date;

    private double minTicketPrice;

    private double avgTicketPrice;

    private double percentile25;

    private double percentile10;

    private double percentile5;
}
