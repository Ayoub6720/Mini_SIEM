package com.mini_siem;

import java.util.List;

public class ReportData {
    private final List<IpStats> ipStats;
    private final List<BruteForceFinding> bruteForceFindings;
    private final Summary summary;

    public ReportData(List<IpStats> ipStats, List<BruteForceFinding> bruteForceFindings, Summary summary) {
        this.ipStats = ipStats;
        this.bruteForceFindings = bruteForceFindings;
        this.summary = summary;
    }

    public List<IpStats> getIpStats() {
        return ipStats;
    }

    public List<BruteForceFinding> getBruteForceFindings() {
        return bruteForceFindings;
    }

    public Summary getSummary() {
        return summary;
    }
}
