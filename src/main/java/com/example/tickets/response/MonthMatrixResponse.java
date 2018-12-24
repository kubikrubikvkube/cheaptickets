package com.example.tickets.response;

import com.example.tickets.ticket.Ticket;
import lombok.Data;

import java.util.List;

/**
 * Возвращает цены за каждый день месяца, сгруппированные по количеству пересадок.
 */
@Data
public class MonthMatrixResponse {
    private Boolean success;
    private List<Ticket> data;
    private String error;
}
