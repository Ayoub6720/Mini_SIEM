package com.mini_siem;

import java.io.IOException;
import java.nio.file.Path;

public interface ReportGenerator {
    void generate(ReportData data, Path outputDir) throws IOException;
}
