package com.mini_siem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BruteForceDetector implements DetectionRule<List<BruteForceFinding>> {
    private final int threshold;
    private final Duration window;

    public BruteForceDetector(int threshold, Duration window) {
        this.threshold = threshold;
        this.window = window;
    }

    @Override
    public List<BruteForceFinding> analyze(List<LogEntry> entries) {
        Map<String, List<LogEntry>> byIp = entries.stream()
            .filter(e -> e.isFail() && "LOGIN".equalsIgnoreCase(e.getEvent()))
            .collect(Collectors.groupingBy(LogEntry::getIp));

        List<BruteForceFinding> findings = new ArrayList<>();

        for (Map.Entry<String, List<LogEntry>> entry : byIp.entrySet()) {
            String ip = entry.getKey();
            List<LogEntry> ipEntries = entry.getValue();
            ipEntries.sort(Comparator.comparing(LogEntry::getTimestamp));

            Deque<LocalDateTime> windowQueue = new ArrayDeque<>();
            LocalDateTime firstDetected = null;
            int maxFailsInWindow = 0;
            LocalDateTime maxWindowStart = null;
            LocalDateTime maxWindowEnd = null;

            for (LogEntry logEntry : ipEntries) {
                LocalDateTime ts = logEntry.getTimestamp();

                while (!windowQueue.isEmpty() && Duration.between(windowQueue.peekFirst(), ts).compareTo(window) > 0) {
                    windowQueue.pollFirst();
                }

                windowQueue.addLast(ts);

                if (windowQueue.size() >= threshold) {
                    if (firstDetected == null) {
                        firstDetected = ts;
                    }
                    if (windowQueue.size() > maxFailsInWindow) {
                        maxFailsInWindow = windowQueue.size();
                        maxWindowStart = windowQueue.peekFirst();
                        maxWindowEnd = ts;
                    }
                }
            }

            if (firstDetected != null) {
                findings.add(new BruteForceFinding(ip, firstDetected, maxFailsInWindow, maxWindowStart, maxWindowEnd));
            }
        }

        findings.sort(Comparator.comparingInt(BruteForceFinding::getMaxFailsInWindow).reversed());
        return findings;
    }
}
