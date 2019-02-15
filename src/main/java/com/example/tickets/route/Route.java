package com.example.tickets.route;

import com.example.tickets.ticket.Ticket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_route", columnList = "id,origin,destination,sumValue,tripDurationInDays")})
public class Route {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Место отправления
     */
    private String origin;

    /**
     * Пункт назначения
     */
    private String destination;

    /**
     * Билет из места отправления
     */
    @OneToOne
    private Ticket departTicket;

    /**
     * Обратный билет
     */
    @OneToOne
    private Ticket returnTicket;
    /**
     * Общая стоимость поездки
     */
    private Integer sumValue;

    private String affilateLink;
    /**
     * Время поездки в днях
     */
    private Integer tripDurationInDays;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
