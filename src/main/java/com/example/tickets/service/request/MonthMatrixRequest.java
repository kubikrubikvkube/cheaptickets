package com.example.tickets.service.request;

import lombok.Builder;
import lombok.Value;

/**
 * Календарь цен на месяц
 * <p>
 * Возвращает цены за каждый день месяца, сгруппированные по количеству пересадок.
 */
@Builder
@Value
public class MonthMatrixRequest {
    /**
     * Валюта цен на билеты. Значение по умолчанию — rub.
     */
    private String currency;

    /**
     * IATA код города. Длина 3 символов. Значение по умолчанию LED.
     */
    private String origin;

    /**
     * IATA код города. Длина 3 символа. Значение по умолчанию HKT.
     * Обратите внимание!
     * Если не указывать пункт отправления и назначения, то API вернет список самых дешевых билетов, которые были найдены за последние 48 часов.
     */
    private String destination;

    /**
     * False — все цены, true — только цены, найденные с партнёрским маркером (рекомендовано).
     * Значение по умолчанию — true.
     */
    private Boolean show_to_affiliates;

    /**
     * Первый день месяца, в формате «YYYY-MM-DD». По умолчанию используется месяц, следующий за текущим.
     */
    private String month;

    /**
     * Длительность пребывания в неделях. Если не указано, то в результате будут билеты в одну сторону.
     */
    private Integer trip_duration;
}
