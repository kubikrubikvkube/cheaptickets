package com.example.tickets.aviasales;

import com.example.tickets.ticket.TicketDto;
import lombok.Data;

import java.util.List;

@Data
class AviasalesResponse {
    List<TicketDto> prices;
    private String error;
}
