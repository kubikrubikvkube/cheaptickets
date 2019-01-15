package com.example.tickets.subscription;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
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
