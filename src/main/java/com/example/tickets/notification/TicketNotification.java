package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.example.tickets.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table
public class TicketNotification {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @OneToOne
    Route route;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner_fk")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Subscription subscription;
}
