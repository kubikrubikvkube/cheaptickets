package com.example.tickets.repository.subscription;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_subscription", columnList = "id,owner,origin,destination")})
public class Subscription {
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
