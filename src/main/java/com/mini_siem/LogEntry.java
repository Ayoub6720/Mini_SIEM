package com.mini_siem;

import java.time.LocalDateTime;

public class LogEntry {
    private final LocalDateTime timestamp;
    private final String ip;
    private final String event;
    private final String status;
    private final String message;
    private final String rawLine;

    public LogEntry(LocalDateTime timestamp, String ip, String event, String status, String message, String rawLine) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.event = event;
        this.status = status;
        this.message = message;
        this.rawLine = rawLine;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getIp() {
        return ip;
    }

    public String getEvent() {
        return event;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getRawLine() {
        return rawLine;
    }

    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status);
    }

    public boolean isFail() {
        return "FAIL".equalsIgnoreCase(status);
    }
}
