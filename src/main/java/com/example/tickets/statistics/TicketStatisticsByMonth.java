package com.example.tickets.statistics;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Month;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket_statistics_by_month", columnList = "id,month")})
public class TicketStatisticsByMonth {
    @Id
    @GeneratedValue
    private Long id;

    private Month month;

    private int ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile10;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
