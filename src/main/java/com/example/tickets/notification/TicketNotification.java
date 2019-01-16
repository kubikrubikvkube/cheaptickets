package com.example.tickets.notification;

import com.example.tickets.route.Route;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table
public class TicketNotification {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Date creationTimestamp;

    @ManyToOne
    Route ticketNotification;
    private String owner;
}
