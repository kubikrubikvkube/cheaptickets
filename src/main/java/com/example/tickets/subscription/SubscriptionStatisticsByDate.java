package com.example.tickets.subscription;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table
public class SubscriptionStatisticsByDate {
    /**
     * PK айдишник базы
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = SubscriptionStatistics.class)
    @PrimaryKeyJoinColumn
    private Long subscriptionStatisticsId;

    @Column
    private LocalDate date;

    @Column
    private double minTicketPrice;
}
