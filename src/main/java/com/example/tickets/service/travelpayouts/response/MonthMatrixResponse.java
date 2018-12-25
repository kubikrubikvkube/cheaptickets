package com.example.tickets.service.travelpayouts.response;

import com.example.tickets.service.TicketJson;
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
