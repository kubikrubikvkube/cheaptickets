package com.example.tickets.ticket;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TicketDTO {
    /**
     * Класс перелёта (только 0 — Эконом);
     */

    private Integer trip_class;
    /**
     * False — все цены, true — только цены, найденные с партнёрским маркером (рекомендовано). Значение по умолчанию — true.
     */

    private Boolean show_to_affiliates;
    /**
     * Пункт отправления.
     */

    private String origin;
    /**
     * Пункт назначения.
     */
    private String destination;
    /**
     * Дата отправления.
     */
    private OffsetDateTime depart_date;
    /**
     * Дата возвращения.
     */
    private OffsetDateTime return_date;
    /**
     * Количество пересадок.
     */
    private Integer number_of_changes;
    /**
     * Стоимость перелета, в указанной валюте.
     */
    private Integer value;
    /**
     * Время и дата, когда был найден билет.
     */
    private OffsetDateTime found_at;
    /**
     * Расстояние между пунктом вылета и назначения.
     */
    private Integer distance;
    /**
     * Является ли предложение актуальным.
     */
    private Boolean actual;
    /**
     * Агентство, в котором был найден билет.
     */
    private String gate;
    /**
     * IATA код авиакомпании, выполняющей перелет.
     */
    private String airline;
    /**
     * Количество пересадок.
     */
    private Integer transfers;


    /**
     * Номер рейса
     */
    private String flightNumber;

    /**
     * TimeToLive in milliseconds
     */
    private Long ttl;

    /**
     * Время истекания срока действия билета
     */
    private OffsetDateTime expiresAt;

    /**
     * Время, когда билет был отдан пользователю через API (?)
     */
    private Long created_at;

    /**
     * Помечен ли билет как expires
     */
    private Boolean isExpired;
}
