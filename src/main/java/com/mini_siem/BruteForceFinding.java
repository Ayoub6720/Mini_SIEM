package com.mini_siem;

import java.time.LocalDateTime;

public class BruteForceFinding {
    private final String ip;
    private final LocalDateTime firstDetected;
    private final int maxFailsInWindow;
    private final LocalDateTime windowStart;
    private final LocalDateTime windowEnd;

    public BruteForceFinding(String ip, LocalDateTime firstDetected, int maxFailsInWindow, LocalDateTime windowStart, LocalDateTime windowEnd) {
        this.ip = ip;
        this.firstDetected = firstDetected;
        this.maxFailsInWindow = maxFailsInWindow;
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
    }

    public String getIp() {
        return ip;
    }

    public LocalDateTime getFirstDetected() {
        return firstDetected;
    }

    public int getMaxFailsInWindow() {
        return maxFailsInWindow;
    }

    public LocalDateTime getWindowStart() {
        return windowStart;
    }

    public LocalDateTime getWindowEnd() {
        return windowEnd;
    }
}
