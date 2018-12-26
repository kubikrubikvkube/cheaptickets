package com.example.tickets.service.aviasales.response;

import com.example.tickets.service.TicketDTO;
import lombok.Data;

import java.util.List;

@Data
public class AviasalesResponse {
    List<TicketDTO> prices;
    private String error;
}
