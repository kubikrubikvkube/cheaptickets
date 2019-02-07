package com.example.tickets.travelpayouts.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

/**
 * Билет без пересадок
 * <p>
 * Возвращает самый дешевый билет без пересадок для выбранного направления с фильтрами по датам вылета и возвращения.
 */

@Value
@Builder
public class DirectRequest {
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
     * Месяц вылета (YYYY-MM) или день вылета (YYYY-MM-DD)
     */
    @JsonProperty("depart_date")
    private LocalDate departDate;

    /**
     * Месяц возвращения (YYYY-MM) или день возвращения (YYYY-MM-DD)
     */
    @JsonProperty("return_date")
    private LocalDate returnDate;

    @Override
    public String toString() {
        UriComponentsBuilder queryBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.travelpayouts.com")
                .path("/v1/prices/direct");

        if (currency != null) queryBuilder.queryParam(currency);
        if (origin != null) queryBuilder.queryParam(origin);
        if (destination != null) queryBuilder.queryParam(destination);
        if (departDate != null) queryBuilder.queryParam(departDate.toString());
        if (returnDate != null) queryBuilder.queryParam(returnDate.toString());

        return queryBuilder.build(true).toUriString();
    }
}
