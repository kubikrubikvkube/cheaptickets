package com.example.tickets.subscription;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

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
    private Date departDate;

    /**
     * Дата возвращения
     */
    private Date returnDate;

    /**
     * Дата истечения подписки
     */
    private Date expirationDate;

    /**
     * Истекла ли подписка
     */
    private Boolean isExpired;
}
