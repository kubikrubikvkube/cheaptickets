package com.example.tickets.latestprices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Возвращает список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LatestPricesGetResponse {
    public Boolean success;
    public List<Offer> data;
}
