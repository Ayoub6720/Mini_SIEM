package com.mini_siem;

import java.util.List;

public interface DetectionRule<T> {
    T analyze(List<LogEntry> entries);
}
