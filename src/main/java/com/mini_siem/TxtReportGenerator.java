package com.mini_siem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class TxtReportGenerator implements ReportGenerator {
    @Override
    public void generate(ReportData data, Path outputDir) throws IOException {
        StringBuilder sb = new StringBuilder();
        Summary summary = data.getSummary();

        sb.append("=== Mini-SIEM Report ===\n");
        sb.append("Files processed: ").append(summary.getFilesProcessed()).append("\n");
        sb.append("Total events: ").append(summary.getTotalEvents()).append("\n");
        sb.append("Success events: ").append(summary.getSuccessEvents()).append("\n");
        sb.append("Fail events: ").append(summary.getFailEvents()).append("\n");
        sb.append("Invalid lines: ").append(summary.getInvalidLines()).append("\n\n");

        sb.append("--- Brute-force suspects ---\n");
        if (data.getBruteForceFindings().isEmpty()) {
            sb.append("None detected\n\n");
        } else {
            for (BruteForceFinding finding : data.getBruteForceFindings()) {
                sb.append("IP: ").append(finding.getIp())
                    .append(" | First detected: ").append(DateUtils.formatTimestamp(finding.getFirstDetected()))
                    .append(" | Max fails in window: ").append(finding.getMaxFailsInWindow())
                    .append(" | Window: ").append(DateUtils.formatTimestamp(finding.getWindowStart()))
                    .append(" -> ").append(DateUtils.formatTimestamp(finding.getWindowEnd()))
                    .append("\n");
            }
            sb.append("\n");
        }

        sb.append("--- Per-IP statistics (sorted by total events) ---\n");
        for (IpStats stats : data.getIpStats()) {
            sb.append("IP: ").append(stats.getIp()).append("\n");
            sb.append("  Total: ").append(stats.getTotalEvents())
                .append(" | Success: ").append(stats.getSuccessEvents())
                .append(" | Fail: ").append(stats.getFailEvents()).append("\n");
            sb.append("  First seen: ").append(DateUtils.formatTimestamp(stats.getFirstSeen())).append("\n");
            sb.append("  Last seen: ").append(DateUtils.formatTimestamp(stats.getLastSeen())).append("\n");
            sb.append("  Event types: ")
                .append(joinTypes(stats.getEventTypes().stream().sorted().collect(Collectors.toList())))
                .append("\n\n");
        }

        Path outFile = outputDir.resolve("report.txt");
        Files.writeString(outFile, sb.toString());
    }

    private String joinTypes(List<String> types) {
        if (types.isEmpty()) {
            return "";
        }
        return String.join(", ", types);
    }
}
