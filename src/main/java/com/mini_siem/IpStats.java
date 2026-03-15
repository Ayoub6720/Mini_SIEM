package com.mini_siem;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class IpStats {
    private final String ip;
    private int totalEvents;
    private int successEvents;
    private int failEvents;
    private final Set<String> eventTypes = new HashSet<>();
    private LocalDateTime firstSeen;
    private LocalDateTime lastSeen;

    public IpStats(String ip) {
        this.ip = ip;
    }

    public void update(LogEntry entry) {
        totalEvents++;
        if (entry.isSuccess()) {
            successEvents++;
        }
        if (entry.isFail()) {
            failEvents++;
        }
        eventTypes.add(entry.getEvent());

        LocalDateTime ts = entry.getTimestamp();
        if (firstSeen == null || ts.isBefore(firstSeen)) {
            firstSeen = ts;
        }
        if (lastSeen == null || ts.isAfter(lastSeen)) {
            lastSeen = ts;
        }
    }

    public String getIp() {
        return ip;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public int getSuccessEvents() {
        return successEvents;
    }

    public int getFailEvents() {
        return failEvents;
    }

    public Set<String> getEventTypes() {
        return eventTypes;
    }

    public LocalDateTime getFirstSeen() {
        return firstSeen;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }
}
