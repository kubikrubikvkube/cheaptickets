package com.example.tickets.travelpayouts.request;

import lombok.Builder;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;


/**
 * Цены на авиабилеты
 * <p>
 * Возвращает список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 * Если не указывать пункт отправления и назначения, то API вернет 30 самых дешевых билетов, которые были найдены за последние 48 часов.
 * При этом нельзя использовать period_type=month.
 */


@Value
@Builder
public class LatestRequest {
    private static final String BASE_URL = "http://api.travelpayouts.com/v2/prices/latest";
    /**
     * Валюта цен на билеты. Значение по умолчанию — rub.
     */
    private String currency;
    /**
     * Пункт отправления. Iata код города или код страны. Длина не менее 2 и не более 3 символов.
     */
    private String origin;
    /**
     * Пункт назначения. Iata код города или код страны. Длина не менее 2 и не более 3.
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
        UriComponentsBuilder queryBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.travelpayouts.com")
                .path("/v2/prices/latest");

        if (currency != null) queryBuilder.queryParam(currency);
        if (origin != null) queryBuilder.queryParam(origin);
        if (destination != null) queryBuilder.queryParam(destination);
        if (beginning_of_period != null) queryBuilder.queryParam(String.valueOf(beginning_of_period));
        if (period_type != null) queryBuilder.queryParam(period_type);
        if (one_way != null) queryBuilder.queryParam(String.valueOf(one_way));
        if (page != null) queryBuilder.queryParam(String.valueOf(page));
        if (limit != null) queryBuilder.queryParam(String.valueOf(limit));
        if (show_to_affiliates != null) queryBuilder.queryParam(String.valueOf(show_to_affiliates));
        if (sorting != null) queryBuilder.queryParam(String.valueOf(sorting));
        if (trip_duration != null) queryBuilder.queryParam(String.valueOf(trip_duration));
        return queryBuilder.build(true).toUriString();
    }
}
