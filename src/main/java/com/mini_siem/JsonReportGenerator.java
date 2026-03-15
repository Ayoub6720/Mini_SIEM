package com.mini_siem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class JsonReportGenerator implements ReportGenerator {
    @Override
    public void generate(ReportData data, Path outputDir) throws IOException {
        StringBuilder sb = new StringBuilder();
        Summary summary = data.getSummary();

        sb.append("{\n");
        sb.append("  \"summary\": {");
        sb.append("\n    \"filesProcessed\": ").append(summary.getFilesProcessed()).append(",");
        sb.append("\n    \"totalEvents\": ").append(summary.getTotalEvents()).append(",");
        sb.append("\n    \"successEvents\": ").append(summary.getSuccessEvents()).append(",");
        sb.append("\n    \"failEvents\": ").append(summary.getFailEvents()).append(",");
        sb.append("\n    \"invalidLines\": ").append(summary.getInvalidLines());
        sb.append("\n  },\n");

        sb.append("  \"bruteForceSuspects\": [\n");
        for (int i = 0; i < data.getBruteForceFindings().size(); i++) {
            BruteForceFinding finding = data.getBruteForceFindings().get(i);
            sb.append("    {");
            sb.append("\"ip\": \"").append(escape(finding.getIp())).append("\",");
            sb.append(" \"firstDetected\": \"").append(escape(DateUtils.formatTimestamp(finding.getFirstDetected()))).append("\",");
            sb.append(" \"maxFailsInWindow\": ").append(finding.getMaxFailsInWindow()).append(",");
            sb.append(" \"windowStart\": \"").append(escape(DateUtils.formatTimestamp(finding.getWindowStart()))).append("\",");
            sb.append(" \"windowEnd\": \"").append(escape(DateUtils.formatTimestamp(finding.getWindowEnd()))).append("\"}");
            if (i < data.getBruteForceFindings().size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ],\n");

        sb.append("  \"ipStats\": [\n");
        for (int i = 0; i < data.getIpStats().size(); i++) {
            IpStats stats = data.getIpStats().get(i);
            sb.append("    {");
            sb.append("\"ip\": \"").append(escape(stats.getIp())).append("\",");
            sb.append(" \"totalEvents\": ").append(stats.getTotalEvents()).append(",");
            sb.append(" \"successEvents\": ").append(stats.getSuccessEvents()).append(",");
            sb.append(" \"failEvents\": ").append(stats.getFailEvents()).append(",");
            sb.append(" \"firstSeen\": \"").append(escape(DateUtils.formatTimestamp(stats.getFirstSeen()))).append("\",");
            sb.append(" \"lastSeen\": \"").append(escape(DateUtils.formatTimestamp(stats.getLastSeen()))).append("\",");
            String eventTypes = stats.getEventTypes().stream().sorted().collect(Collectors.joining("|"));
            sb.append(" \"eventTypes\": \"").append(escape(eventTypes)).append("\"}");
            if (i < data.getIpStats().size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}\n");

        Path outFile = outputDir.resolve("report.json");
        Files.writeString(outFile, sb.toString());
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
