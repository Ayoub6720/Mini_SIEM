package com.mini_siem;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    private static final Pattern LINE_PATTERN = Pattern.compile(
        "^\\[(.+?)\\]\\s+IP=(\\S+)\\s+EVENT=(\\S+)\\s+STATUS=(\\S+)\\s+MESSAGE=(.*)$"
    );

    public ParseResult parseFile(Path file) {
        List<LogEntry> entries = new ArrayList<>();
        int invalidLines = 0;

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogEntry entry = parseLine(line);
                if (entry == null) {
                    invalidLines++;
                } else {
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            return new ParseResult(entries, invalidLines, file, e);
        }

        return new ParseResult(entries, invalidLines, file, null);
    }

    private LogEntry parseLine(String line) {
        Matcher matcher = LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return null;
        }

        String tsRaw = matcher.group(1);
        String ip = matcher.group(2);
        String event = matcher.group(3);
        String status = matcher.group(4);
        String message = matcher.group(5);

        LocalDateTime timestamp = DateUtils.parseTimestamp(tsRaw);
        if (timestamp == null) {
            return null;
        }

        return new LogEntry(timestamp, ip, event, status, message, line);
    }
}
