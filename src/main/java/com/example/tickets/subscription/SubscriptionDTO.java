package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SubscriptionDTO {
    /**
     * Кто создал подписку
     */
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
    private Date departDate;

    /**
     * Дата возвращения
     */
    private Date returnDate;

}
