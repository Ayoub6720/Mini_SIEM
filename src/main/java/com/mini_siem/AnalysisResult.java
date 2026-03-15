package com.mini_siem;

import java.util.Map;

public class AnalysisResult {
    private final Map<String, IpStats> statsByIp;
    private final int totalEvents;
    private final int successEvents;
    private final int failEvents;

    public AnalysisResult(Map<String, IpStats> statsByIp, int totalEvents, int successEvents, int failEvents) {
        this.statsByIp = statsByIp;
        this.totalEvents = totalEvents;
        this.successEvents = successEvents;
        this.failEvents = failEvents;
    }

    public Map<String, IpStats> getStatsByIp() {
        return statsByIp;
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
}
