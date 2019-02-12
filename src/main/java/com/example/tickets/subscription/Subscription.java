package com.example.tickets.subscription;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.owner.Owner;
import com.example.tickets.subscription.filteringcriteria.RouteFilteringCriteria;
import com.example.tickets.subscription.filteringcriteria.TicketFilteringCriteria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(indexes = {@Index(name = "idx_subscription", columnList = "id,origin,destination,departDate,returnDate")})
public class Subscription {


    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    /**
     * Кто создал подписку
     */

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner_fk")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Owner owner;
    /**
     * Iata отправления
     */
    @Column(length = 3)
    private String origin;
    /**
     * Iata назначения
     */
    @Column(length = 3)
    private String destination;

    /**
     * Название пункта отправления
     */
    private String originName;

    /**
     * Название пункта назначения
     */
    private String destinationName;

    /**
     * Дата отправления
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departDate;

    /**
     * Дата возвращения
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    /**
     * Время поездки в днях от
     */
    private Integer tripDurationInDaysFrom;

    /**
     * Время поездки в днях до
     */
    private Integer tripDurationInDaysTo;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @CollectionTable(name = "subscription_route_filtering_criteria_set")
    @OneToMany(targetEntity = RouteFilteringCriteria.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<RouteFilteringCriteria> routeFilteringCriteriaSet;

    @CollectionTable(name = "subscription_ticket_filtering_criteria_set")
    @OneToMany(targetEntity = TicketFilteringCriteria.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<TicketFilteringCriteria> ticketFilteringCriteriaSet;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn
    @OneToMany
    private List<RouteNotification> routeNotifications;
}
