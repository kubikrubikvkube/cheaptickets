package com.example.tickets.subscription;

import lombok.Data;

@Data
public class Subscription {
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
