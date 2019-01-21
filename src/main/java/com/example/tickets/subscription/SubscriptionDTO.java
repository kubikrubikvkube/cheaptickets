package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate departDate;

    /**
     * Дата возвращения
     */
    private LocalDate returnDate;

    /**
     * Время поездки в днях
     */
    private Integer tripDurationInDays;
}
