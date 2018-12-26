package com.example.tickets.repository;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_ticket", columnList = "id,origin,destination,departDate,returnDate")})
public class Ticket {
    /**
     * Класс перелёта (только 0 — Эконом);
     */

    private Integer tripClass;
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
    private Calendar catchedOn;
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
     * Время жизни билета в миллисекундах
     */
    private Long ttl;
}
