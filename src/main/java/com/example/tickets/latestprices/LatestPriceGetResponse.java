package com.example.tickets.latestprices;

import lombok.Data;

import java.util.List;

/**
 * Список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 */
@Data
public class LatestPriceGetResponse {
    public Boolean success;
    public List<Ticket> data;
}
