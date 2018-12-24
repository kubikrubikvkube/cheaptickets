package com.example.tickets.response;

import com.example.tickets.ticket.Ticket;
import lombok.Data;

import java.util.List;

/**
 * Список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 */
@Data
public class LatestResponse {
    private Boolean success;
    private List<Ticket> data;
    private String error;
}
