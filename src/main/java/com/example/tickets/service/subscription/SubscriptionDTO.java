package com.example.tickets.service.subscription;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionDTO {
    /**
     * Кто создал подписку
     */
    private final String owner;
    /**
     * IAT отправления
     */
    private final String origin;
    /**
     * IAT назначения
     */
    private final String destination;
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

}
