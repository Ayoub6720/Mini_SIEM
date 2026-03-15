package com.mini_siem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class CsvReportGenerator implements ReportGenerator {
    @Override
    public void generate(ReportData data, Path outputDir) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("ip,total_events,success_events,fail_events,first_seen,last_seen,event_types\n");

        for (IpStats stats : data.getIpStats()) {
            String eventTypes = stats.getEventTypes().stream().sorted().collect(Collectors.joining("|"));
            sb.append(escape(stats.getIp())).append(',')
                .append(stats.getTotalEvents()).append(',')
                .append(stats.getSuccessEvents()).append(',')
                .append(stats.getFailEvents()).append(',')
                .append(escape(DateUtils.formatTimestamp(stats.getFirstSeen()))).append(',')
                .append(escape(DateUtils.formatTimestamp(stats.getLastSeen()))).append(',')
                .append(escape(eventTypes)).append('\n');
        }

        Path outFile = outputDir.resolve("report.csv");
        Files.writeString(outFile, sb.toString());
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n");
        String escaped = value.replace("\"", "\"\"");
        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }
}
