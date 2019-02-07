package com.example.tickets.statistics;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@NoArgsConstructor
public class TicketStatisticsByMonthDto {
    private Month month;

    private Integer year;

    private Integer ticketsCount;

    private Double minTicketPrice;

    private Double avgTicketPrice;

    private Double percentile10;
}
