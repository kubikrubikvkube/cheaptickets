package com.example.tickets.travelpayouts.response;

import com.example.tickets.ticket.TicketDto;
import lombok.Data;

import java.util.List;

/**
 * Список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 */

@Data
public class LatestResponse {
    private Boolean success;
    private List<TicketDto> data;
    private String error;
}
