package com.example.tickets.statistics;

import lombok.Data;

import javax.persistence.*;
import java.time.Month;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket_statistics_by_month", columnList = "id,month,year")})
public class TicketStatisticsByMonth {
    @Id
    @GeneratedValue
    private Long id;

    private Month month;

    private Integer year;

    private Long ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile10;

}
