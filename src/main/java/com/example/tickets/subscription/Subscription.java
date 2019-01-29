package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
     * IAT отправления
     */
    private String origin;
    /**
     * IAT назначения
     */
    private String destination;

    /**
     * Дата отправления
     */
    private LocalDate departDate;

    /**
     * Дата возвращения
     */
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
}
