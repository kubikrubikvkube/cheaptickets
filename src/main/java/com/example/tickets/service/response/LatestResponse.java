package com.example.tickets.service.response;

import com.example.tickets.ticket.TicketJson;
import lombok.Data;

import java.util.List;

/**
 * Список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 */
@Data
public class LatestResponse {
    private Boolean success;
    private List<TicketJson> data;
    private String error;
}
