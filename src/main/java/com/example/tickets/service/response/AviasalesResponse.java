package com.example.tickets.service.response;

import com.example.tickets.ticket.TicketJson;
import lombok.Data;

import java.util.List;

@Data
public class AviasalesResponse {
    List<TicketJson> prices;
    private String error;
}
