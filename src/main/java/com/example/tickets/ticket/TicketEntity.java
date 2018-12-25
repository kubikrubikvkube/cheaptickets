package com.example.tickets.ticket;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ticket")
public class TicketEntity {
    /**
     * PK айдишник базы
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Класс перелёта (только 0 — Эконом);
     */

    private final int tripClass = 0;

    /**
     * False — все цены, true — только цены, найденные с партнёрским маркером (рекомендовано). Значение по умолчанию — true.
     */

    private Boolean showToAffiliates;
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
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date departDate;
    /**
     * Дата возвращения.
     */
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date returnDate;
    /**
     * Количество пересадок.
     */
    private Integer numberOfChanges;
    /**
     * Стоимость перелета, в указанной валюте.
     */
    private Integer value;
    /**
     * Время и дата, когда был найден билет.
     */
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date foundAt;
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
}
