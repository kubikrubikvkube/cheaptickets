package com.example.tickets.service.subscription;

import lombok.Data;

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
}
