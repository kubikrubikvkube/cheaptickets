package com.example.tickets.service.response;

import com.example.tickets.ticket.TicketJson;
import lombok.Data;

import java.util.List;

/**
 * Возвращает цены за каждый день месяца, сгруппированные по количеству пересадок.
 */
@Data
public class MonthMatrixResponse {
    private Boolean success;
    private List<TicketJson> data;
    private String error;
}
