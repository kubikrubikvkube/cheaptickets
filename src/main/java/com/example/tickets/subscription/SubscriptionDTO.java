package com.example.tickets.subscription;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionDTO {
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

    public SubscriptionDTO() {
    }

    public SubscriptionDTO(String owner, String origin, String destination) {
        this.owner = owner;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Дата отправления
     */
    private Date departDate;

    /**
     * Дата возвращения
     */
    private Date returnDate;

}
