package com.example.tickets.subscription;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(targetEntity = SubscriptionStatisticsByDate.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubscriptionStatisticsByDate> subscriptionStatisticsByDate;
}
