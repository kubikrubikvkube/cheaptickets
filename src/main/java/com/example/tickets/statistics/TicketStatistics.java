package com.example.tickets.statistics;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ticket_statistics_routes", indexes = {@Index(name = "idx_ticket_statistics_routes", columnList = "id,origin,destination")})
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
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
