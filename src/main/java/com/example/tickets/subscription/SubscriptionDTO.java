package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SubscriptionDTO {
    /**
     * ID
     */
    private Long id;

    private LocalDateTime creationTimestamp;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departDate;

    /**
     * Дата возвращения
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    /**
     * Время поездки в днях от
     */
    private Integer tripDurationInDaysFrom;

    /**
     * Время поездки в днях до
     */
    private Integer tripDurationInDaysTo;

    private SubscriptionType subscriptionType;
}
