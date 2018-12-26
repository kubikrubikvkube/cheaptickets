package com.example.tickets.service.travelpayouts.response;

import com.example.tickets.service.TicketDTO;
import lombok.Data;

import java.util.List;

/**
 * Список цен, найденных нашими пользователями за последние 48 часов, в соответствии с выставленными фильтрами.
 */
@Data
public class LatestResponse {
    private Boolean success;
    private List<TicketDTO> data;
    private String error;
}
