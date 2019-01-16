package com.example.tickets.statistics;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class TicketStatisticsByDayDTO {
    private LocalDate date;

    private Long ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile10;

    private Date createdAt;

    private Date updatedAt;

}
