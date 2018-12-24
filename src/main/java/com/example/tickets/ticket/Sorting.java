package com.example.tickets.ticket;

import lombok.RequiredArgsConstructor;

/**
 * Sorting — сортировка цен:
 */
@RequiredArgsConstructor
public enum Sorting {
    /**
     * Price — по цене (значение по умолчанию). Для направлений город — город возможна сортировка только по цене;
     */
    PRICE("price"),
    /**
     * Route — по популярности маршрута;
     */
    ROUTE("route"),
    /**
     * Distance_unit_price — по цене за километр.
     */
    DISTANCE_UNIT_PRICE("distance_unit_price");

    private final String lowercase;

    @Override
    public String toString() {
        return lowercase;
    }
}