package com.example.tickets.repository.subscription;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "subscription")
public class SubscriptionEntity {
    /**
     * PK айдишник базы
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Метка даты записи в БД, когда была создана подписка
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Calendar creationDate;

    /**
     * Кто создал подписку
     */
    private String owner;
    /**
     * Откуда
     */
    private String origin;
    /**
     * Куда
     */
    private String destination;
}
