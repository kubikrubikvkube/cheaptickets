package com.example.tickets.subscription;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class SubscriptionStatistics {
    /**
     * PK айдишник базы
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = Subscription.class, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Long subscriptionId;

    @OneToOne(targetEntity = SubscriptionStatisticsByDate.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SubscriptionStatisticsByDate subscriptionStatisticsByDate;
}
