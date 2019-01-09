package com.example.tickets.statistics;

import lombok.Data;

import javax.persistence.*;
import java.time.Month;

@Data
@Entity
@Table
public class TicketStatisticsByMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = TicketStatistics.class)
    @PrimaryKeyJoinColumn
    private Long subscriptionStatisticsId;

    private Month date;

    private double minTicketPrice;

    private double avgTicketPrice;

    private double percentile25;

    private double percentile10;

    private double percentile5;
}
