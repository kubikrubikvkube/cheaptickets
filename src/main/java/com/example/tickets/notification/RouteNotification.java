package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table
public class RouteNotification {
    @OneToOne
    Route route;
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private LocalDateTime creationTimestamp;
    @JsonManagedReference

    @ManyToOne
    @JoinColumn(name = "subscription_fk", nullable = false)
    private Subscription subscription;
}
