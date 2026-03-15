package com.mini_siem;

public class Summary {
    private final int totalEvents;
    private final int successEvents;
    private final int failEvents;
    private final int invalidLines;
    private final int filesProcessed;

    public Summary(int totalEvents, int successEvents, int failEvents, int invalidLines, int filesProcessed) {
        this.totalEvents = totalEvents;
        this.successEvents = successEvents;
        this.failEvents = failEvents;
        this.invalidLines = invalidLines;
        this.filesProcessed = filesProcessed;
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

    public int getInvalidLines() {
        return invalidLines;
    }

    public int getFilesProcessed() {
        return filesProcessed;
    }
}
