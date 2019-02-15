package com.example.tickets.statistics;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "ticket_statistics", indexes = {@Index(name = "idx_ticket_statistics", columnList = "id,origin,destination")})
public class TicketStatistics {
    @Id
    @GeneratedValue
    private Long id;

    private String origin;

    private String destination;

    @CollectionTable(name = "ticket_statistics_by_month_collection")
    @OneToMany(targetEntity = TicketStatisticsByMonth.class, cascade = CascadeType.ALL)
    @LazyCollection(value = LazyCollectionOption.FALSE) //TODO perfomance!
    private List<TicketStatisticsByMonth> ticketStatisticsByMonth;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketStatistics)) return false;
        TicketStatistics that = (TicketStatistics) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
