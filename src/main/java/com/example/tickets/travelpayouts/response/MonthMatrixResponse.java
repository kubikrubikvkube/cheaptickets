package com.example.tickets.travelpayouts.response;

import com.example.tickets.ticket.TicketDto;
import lombok.Data;

import java.util.List;

/**
 * Возвращает цены за каждый день месяца, сгруппированные по количеству пересадок.
 */
@Data
public class MonthMatrixResponse {
    private Boolean success;
    private List<TicketDto> data;
    private String error;
}
