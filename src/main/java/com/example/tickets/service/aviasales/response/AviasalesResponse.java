package com.example.tickets.service.aviasales.response;

import com.example.tickets.service.TicketJson;
import lombok.Data;

import java.util.List;

@Data
public class AviasalesResponse {
    List<TicketJson> prices;
    private String error;
}
