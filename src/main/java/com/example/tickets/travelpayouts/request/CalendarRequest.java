package com.example.tickets.travelpayouts.request;

import lombok.Builder;
import lombok.Value;

/**
 * Билеты из города на любое число месяца
 * <p>
 * Возвращает самый дешевый билет (без пересадки, с одной или двумя пересадками) для указанного направления для каждого дня выбранного месяца.
 */
@Value
@Builder
public class CalendarRequest {
}
