package com.example.tickets.service.subscription;

import lombok.Data;

@Data
public class Subscription {
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
}
