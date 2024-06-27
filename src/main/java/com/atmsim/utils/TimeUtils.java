package com.atmsim.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtils {
    public static LocalDateTime of(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    public static LocalDate of(Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date of(LocalDate date) {
        return new Date(date.toEpochDay());
    }

    public static Timestamp of(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
