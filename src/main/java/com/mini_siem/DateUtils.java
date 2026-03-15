package com.mini_siem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateUtils() {
    }

    public static LocalDateTime parseTimestamp(String raw) {
        try {
            return LocalDateTime.parse(raw, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String formatTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            return "";
        }
        return FORMATTER.format(timestamp);
    }
}
