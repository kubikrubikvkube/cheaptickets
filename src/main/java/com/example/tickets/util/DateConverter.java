package com.example.tickets.util;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class DateConverter {
    @Nullable
    public static Date toDate(LocalDate localDate) {
        Optional<LocalDate> localDateOpt = Optional.ofNullable(localDate);
        return localDateOpt.isPresent() ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }

    @Nullable
    public static Date toDate(String date) {
        Optional<String> dateOpt = Optional.ofNullable(date);
        return dateOpt.isPresent() ? toDate(LocalDate.parse(date)) : null;
    }

    @Nullable
    public static LocalDate toLocalDate(Date date) {
        Optional<Date> dateOpt = Optional.ofNullable(date);
        return dateOpt.isPresent() ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    @Nullable
    public static LocalDate toLocalDate(String date) {
        Optional<String> dateOpt = Optional.ofNullable(date);
        return dateOpt.isPresent() ? LocalDate.parse(date) : null;
    }
}
