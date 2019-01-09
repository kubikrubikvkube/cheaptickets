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
    private Long ticketStatisticsId;

    private Month date;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile5;
}
