package com.example.tickets.service;

import lombok.Data;

import java.util.Date;

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
    private Date depart_date;
    /**
     * Дата возвращения.
     */
    private Date return_date;
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
    private Date found_at;
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
     * TimeToLive in milliseconds
     */
    private Long ttl;

    /**
     * Время, когда билет был отдан пользователю через API (?)
     */
    private Long created_at;
}