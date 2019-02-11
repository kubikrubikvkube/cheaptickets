package com.example.tickets.subscription;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.owner.Owner;
import com.example.tickets.route.filteringcriteria.RouteFilteringCriteria;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
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
    @OneToMany(targetEntity = RouteFilteringCriteria.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<RouteFilteringCriteria> filteringCriteriaSet;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @CollectionTable(name = "subscription_route_notifications")
    @OneToMany(targetEntity = RouteNotification.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RouteNotification> routeNotifications;
}
