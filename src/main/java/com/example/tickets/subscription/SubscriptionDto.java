package com.example.tickets.subscription;

import com.example.tickets.notification.RouteNotification;
import com.example.tickets.owner.Owner;
import com.example.tickets.subscription.filteringcriteria.RouteFilteringCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class SubscriptionDto {
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
     * Название пункта отправления
     */
    private String originName;

    /**
     * Название пункта назначения
     */
    private String destinationName;

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

    private List<RouteNotification> routeNotifications;

    private Set<RouteFilteringCriteria> routeFilteringCriteriaSet;
}
