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
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    Route route;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "subscription_fk")
    private Subscription subscription;
}
