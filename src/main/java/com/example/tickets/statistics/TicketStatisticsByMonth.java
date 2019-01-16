package com.example.tickets.statistics;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket_statistics_by_month", columnList = "id,date")})
public class TicketStatisticsByMonth {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    private Long ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile5;
}
