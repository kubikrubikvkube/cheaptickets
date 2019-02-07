package com.example.tickets.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TicketDto {
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

    @JsonProperty("departDate")
    private LocalDate departDate;
    /**
     * Дата возвращения.
     */
    @JsonProperty("returnDate")
    private LocalDate returnDate;
    /**
     * Количество пересадок.
     */
    @JsonProperty("number_of_changes")
    private Integer numberOfChanges;
    /**
     * Стоимость перелета, в указанной валюте.
     */
    private Integer value;
    /**
     * Время и дата, когда был найден билет.
     */
    @JsonProperty("found_at")
    private LocalDateTime foundAt;
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
     * Iata код авиакомпании, выполняющей перелет.
     */
    private String airline;
    /**
     * Количество пересадок.
     */
    private Integer transfers;

    /**
     * Время отправления.
     */
    @JsonProperty("depart_time")
    private LocalTime departTime;

    /**
     * Номер рейса
     */
    @JsonProperty("flight_number")
    private String flightNumber;

    /**
     * TimeToLive in milliseconds
     */
    private Long ttl;


    /**
     * Время истекания срока действия билета
     */
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;

    /**
     * Время, когда билет был отдан пользователю через API (?)
     */
    @JsonProperty("created_at")
    private Long createdAt;

    /**
     * Помечен ли билет как expires
     */
    @JsonProperty("is_expired")
    private Boolean isExpired;
}
