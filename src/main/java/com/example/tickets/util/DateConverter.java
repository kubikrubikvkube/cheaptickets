package com.example.tickets.util;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class DateConverter {
    @Nullable
    public static OffsetDateTime toDate(LocalDate localDate) {
        Optional<LocalDate> localDateOpt = Optional.ofNullable(localDate);
        return localDateOpt.isPresent() ? OffsetDateTime.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }

    @Nullable
    public static OffsetDateTime toDate(String date) {
        Optional<String> dateOpt = Optional.ofNullable(date);
        return dateOpt.isPresent() ? toDate(LocalDate.parse(date)) : null;
    }

    @Nullable
    public static LocalDate toLocalDate(OffsetDateTime date) {
        Optional<OffsetDateTime> dateOpt = Optional.ofNullable(date);
        return dateOpt.isPresent() ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    @Nullable
    public static LocalDate toLocalDate(String date) {
        Optional<String> dateOpt = Optional.ofNullable(date);
        return dateOpt.isPresent() ? LocalDate.parse(date) : null;
    }
}
