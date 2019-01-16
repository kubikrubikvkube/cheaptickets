package com.example.tickets.statistics;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket_statistics_by_day", columnList = "id,date")})
public class TicketStatisticsByDay {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    private Long ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile10;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
