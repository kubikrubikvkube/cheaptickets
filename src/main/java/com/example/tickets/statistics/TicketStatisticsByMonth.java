package com.example.tickets.statistics;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket_statistics_by_month", columnList = "id,month")})
public class TicketStatisticsByMonth {
    @Id
    @GeneratedValue
    private Long id;

    private Month month;

    private Integer year;

    private Integer ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile10;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketStatisticsByMonth)) return false;
        TicketStatisticsByMonth byMonth = (TicketStatisticsByMonth) o;
        return Objects.equals(id, byMonth.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
