package com.mini_siem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SecurityAnalyzer {
    public AnalysisResult analyze(Collection<LogEntry> entries) {
        Map<String, IpStats> statsByIp = new HashMap<>();
        int total = 0;
        int success = 0;
        int fail = 0;

        for (LogEntry entry : entries) {
            total++;
            if (entry.isSuccess()) {
                success++;
            }
            if (entry.isFail()) {
                fail++;
            }

            IpStats stats = statsByIp.computeIfAbsent(entry.getIp(), IpStats::new);
            stats.update(entry);
        }

        return new AnalysisResult(statsByIp, total, success, fail);
    }
}
