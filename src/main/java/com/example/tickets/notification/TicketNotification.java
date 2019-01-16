package com.example.tickets.notification;

import com.example.tickets.route.Route;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket_notification", columnList = "id,owner")})
public class TicketNotification {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Date creationTimestamp;

    @ManyToOne
    @JsonManagedReference
    Route ticketNotification;

    private String owner;
}
