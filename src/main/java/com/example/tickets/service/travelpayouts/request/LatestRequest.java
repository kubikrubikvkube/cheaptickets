package com.example.tickets.service.travelpayouts.request;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;


/**
 * Цены на авиабилеты
 * <p>
 * Возвращает список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 * Если не указывать пункт отправления и назначения, то API вернет 30 самых дешевых билетов, которые были найдены за последние 48 часов.
 * При этом нельзя использовать period_type=month.
 */
@Builder
@Value
public class LatestRequest {
    private final static String BASE_URL = "http://api.travelpayouts.com/v2/prices/latest";
    /**
     * Валюта цен на билеты. Значение по умолчанию — rub.
     */
    private String currency;
    /**
     * Пункт отправления. IATA код города или код страны. Длина не менее 2 и не более 3 символов.
     */
    private String origin;
    /**
     * Пункт назначения. IATA код города или код страны. Длина не менее 2 и не более 3.
     */
    private String destination;
    /**
     * Первое число месяца, в который попадают даты отправления (в формате YYYY-MM-DD, например 2018-05-01).
     * Обязательно указывать при period_type равном month. Если указать только beginning_of_period, то period_type можно не указывать.
     */
    private LocalDate beginning_of_period;
    /**
     * период, в котором искали билеты. Если период не указан, то отображаются билеты для перелётов в текущем месяце.
     * year — билеты, найденные в текущем году;
     * month — за указанный в beginning_of_period месяц.
     */
    private String period_type;
    /**
     * true — в одну сторону, false — туда и обратно. Значение по умолчанию — false.
     */
    private Boolean one_way;
    /**
     * Page — номер страницы. Значение по умолчанию — 1.
     */
    private Integer page;
    /**
     * Limit— количество записей на странице. Значение по умолчанию — 30. Не более 1000.
     */
    private Integer limit;
    /**
     * False — все цены, true — только цены, найденные с партнёрским маркером (рекомендовано).
     * Значение по умолчанию — true.
     */
    private Boolean show_to_affiliates;
    /**
     * Sorting — сортировка цен:
     * price — по цене (значение по умолчанию). Для направлений город — город возможна сортировка только по цене;
     * route — по популярности маршрута;
     * distance_unit_price — по цене за километр.
     */
    private Sorting sorting;
    /**
     * Длительность пребывания в неделях.
     */
    private Integer trip_duration;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL).append("?");
        if (currency != null) sb.append("currency=").append(currency).append("&");
        if (origin != null) sb.append("origin=").append(origin).append("&");
        if (destination != null) sb.append("destination=").append(destination).append("&");
        if (beginning_of_period != null) sb.append("beginning_of_period=").append(beginning_of_period).append("&");
        if (period_type != null) sb.append("period_type=").append(period_type).append("&");
        if (one_way != null) sb.append("one_way=").append(one_way).append("&");
        if (page != null) sb.append("page=").append(page).append("&");
        if (limit != null) sb.append("limit=").append(limit).append("&");
        if (show_to_affiliates != null) sb.append("show_to_affiliates=").append(show_to_affiliates).append("&");
        if (sorting != null) sb.append("sorting=").append(sorting).append("&");
        if (trip_duration != null) sb.append("trip_duration=").append(trip_duration).append("&");
        sb.deleteCharAt(sb.lastIndexOf("&"));
        return sb.toString();
    }
}
