package com.example.tickets.aviasales.response;

import com.example.tickets.ticket.TicketDTO;
import lombok.Data;

import java.util.List;

@Data
public class AviasalesResponse {
    List<TicketDTO> prices;
    private String error;
}
