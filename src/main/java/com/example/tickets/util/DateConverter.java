package com.example.tickets.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


public class DateConverter {
    public static ZoneOffset localOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
    public static OffsetDateTime midnight(LocalDate date) {
        return date.atTime(0, 0, 0).atOffset(ZoneOffset.UTC);
    }
}
