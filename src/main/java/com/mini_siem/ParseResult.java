package com.mini_siem;

import java.nio.file.Path;
import java.util.List;

public class ParseResult {
    private final List<LogEntry> entries;
    private final int invalidLines;
    private final Path sourceFile;
    private final Exception error;

    public ParseResult(List<LogEntry> entries, int invalidLines, Path sourceFile, Exception error) {
        this.entries = entries;
        this.invalidLines = invalidLines;
        this.sourceFile = sourceFile;
        this.error = error;
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public int getInvalidLines() {
        return invalidLines;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public Exception getError() {
        return error;
    }
}
