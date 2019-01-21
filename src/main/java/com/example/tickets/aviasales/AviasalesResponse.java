package com.example.tickets.aviasales;

import com.example.tickets.ticket.TicketDTO;
import lombok.Data;

import java.util.List;

@Data
class AviasalesResponse {
    List<TicketDTO> prices;
    private String error;
}
