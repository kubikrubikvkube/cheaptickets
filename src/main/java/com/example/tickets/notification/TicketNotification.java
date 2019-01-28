package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket_notification", columnList = "id,owner")})
public class TicketNotification {
    @OneToOne
    Route route;
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @JsonManagedReference
    private String owner;
}
