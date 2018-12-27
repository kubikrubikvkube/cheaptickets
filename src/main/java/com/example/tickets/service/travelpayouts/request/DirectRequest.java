package com.example.tickets.service.travelpayouts.request;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * Билет без пересадок
 * <p>
 * Возвращает самый дешевый билет без пересадок для выбранного направления с фильтрами по датам вылета и возвращения.
 */

@Value
@Builder
public class DirectRequest {
    public final static String BASE_URL = "http://api.travelpayouts.com/v1/prices/direct";
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
     * Месяц вылета (YYYY-MM) или день вылета (YYYY-MM-DD)
     */
    private LocalDate depart_date;

    /**
     * Месяц возвращения (YYYY-MM) или день возвращения (YYYY-MM-DD)
     */
    private LocalDate return_date;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL).append("?");
        if (currency != null) sb.append("currency=").append(currency).append("&");
        if (origin != null) sb.append("origin=").append(origin).append("&");
        if (destination != null) sb.append("destination=").append(destination).append("&");
        if (depart_date != null) sb.append("depart_date=").append(depart_date).append("&");
        if (return_date != null) sb.append("return_date=").append(return_date).append("&");
        sb.deleteCharAt(sb.lastIndexOf("&"));
        return sb.toString();
    }
}
