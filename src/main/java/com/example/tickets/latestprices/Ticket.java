package com.example.tickets.latestprices;

import lombok.Data;

@Data
public class Ticket {

    /**
     * False — все цены, true — только цены, найденные с партнёрским маркером (рекомендовано). Значение по умолчанию — true.
     */
    public Boolean show_to_affiliates;

    /**
     * Пункт отправления.
     */
    public String origin;

    /**
     * Пункт назначения.
     */
    public String destination;

    /**
     * Дата отправления.
     */
    public String depart_date;

    /**
     * Дата возвращения.
     */
    public String return_date;

    /**
     * Количество пересадок.
     */
    public Integer number_of_changes;

    /**
     * Стоимость перелета, в указанной валюте.
     */
    public Integer value;

    /**
     * Время и дата, когда был найден билет.
     */
    public String found_at;

    /**
     * Расстояние между пунктом вылета и назначения.
     */
    public Integer distance;

    /**
     * Является ли предложение актуальным.
     */
    public Boolean actual;


}
