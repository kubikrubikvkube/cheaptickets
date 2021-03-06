package com.example.tickets.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(indexes = {@Index(name = "idx_ticket", columnList = "id,origin,destination,departDate,numberOfChanges,value,actual,isExpired")})
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;
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
     * Название пункта отправления
     */
    private String originName;

    /**
     * Название пункта назначения
     */
    private String destinationName;


    /**
     * Дата отправления.
     */
    @JsonProperty("depart_date")
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
     * Iata код авиакомпании, выполняющей перелет.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
