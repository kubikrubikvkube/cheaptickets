package com.example.tickets.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DateConverter {

    public static OffsetDateTime midnight(LocalDate date) {
        return date.atTime(0, 0, 0).atOffset(ZoneOffset.UTC);
    }
}
