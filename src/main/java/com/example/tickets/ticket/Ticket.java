package com.example.tickets.ticket;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket", columnList = "id,origin,destination,departDate,numberOfChanges,value,actual,isExpired")})
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Date creationTimestamp;
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
    private LocalDate departDate;
    /**
     * Время отправления.
     */
    private LocalTime departTime;
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
     * Дата возвращения.
     */
    private LocalDate returnDate;

    /**
     * Время истекания срока действия билета
     */
    private LocalDateTime expiresAt;

    /**
     * Время, когда билет был отдан пользователю через API (?)
     */
    private Long createdAt;

    private Boolean isExpired;

}
