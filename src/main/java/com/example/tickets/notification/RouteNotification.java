package com.example.tickets.notification;

import com.example.tickets.route.Route;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteNotification)) return false;
        RouteNotification that = (RouteNotification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
