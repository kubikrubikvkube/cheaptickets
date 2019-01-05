package com.example.tickets.ticket;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket", columnList = "origin,destination,departDate,value,returnDate")})
public class Ticket {
    /**
     * PK айдишник базы
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Метка даты записи в БД, когда был "пойман" билет.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date catchedOn;
    /**
     * Класс перелёта (только 0 — Эконом);
     */
    private Integer tripClass;

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

    /**
     * Номер рейса
     */
    private String flightNumber;

    /**
     * Время жизни билета в миллисекундах
     */
    private Long ttl;

    /**
     * Время истекания срока действия билета
     */
    private Date expiresAt;

    /**
     * Время, когда билет был отдан пользователю через API (?)
     */
    private Long created_at;
}
