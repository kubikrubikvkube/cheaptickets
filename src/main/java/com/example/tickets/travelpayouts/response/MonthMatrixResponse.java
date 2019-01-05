package com.example.tickets.travelpayouts.response;

import com.example.tickets.ticket.TicketDTO;
import lombok.Data;

import java.util.List;

/**
 * Возвращает цены за каждый день месяца, сгруппированные по количеству пересадок.
 */
@Data
public class MonthMatrixResponse {
    private Boolean success;
    private List<TicketDTO> data;
    private String error;
}
