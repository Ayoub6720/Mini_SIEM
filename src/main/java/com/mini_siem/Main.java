package com.mini_siem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> parsedArgs = parseArgs(args);
        if (parsedArgs.containsKey("help")) {
            printUsage();
            return;
        }

        String inputArg = parsedArgs.get("input");
        String outputArg = parsedArgs.getOrDefault("output", "reports");
        int threshold = parseIntOrDefault(parsedArgs.get("threshold"), 5);
        int windowMinutes = parseIntOrDefault(parsedArgs.get("window"), 10);

        if (threshold <= 0 || windowMinutes <= 0) {
            System.err.println("Threshold and window must be positive integers.");
            printUsage();
            return;
        }

        if (inputArg == null) {
            Path defaultSample = Path.of("samples", "sample.log");
            if (Files.exists(defaultSample)) {
                inputArg = defaultSample.toString();
                System.out.println("No input provided. Using default sample: " + inputArg);
            } else {
                System.err.println("Missing required input path.");
                printUsage();
                return;
            }
        }

        Path inputPath = Path.of(inputArg);
        Path outputDir = Path.of(outputArg);

        List<Path> files;
        try {
            files = FileUtils.collectLogFiles(inputPath);
        } catch (IOException e) {
            System.err.println("Failed to read input path: " + e.getMessage());
            return;
        }

        if (files.isEmpty()) {
            System.err.println("No log files found at: " + inputPath);
            return;
        }

        LogParser parser = new LogParser();
        List<LogEntry> allEntries = new ArrayList<>();
        int invalidLines = 0;
        int filesProcessed = 0;

        for (Path file : files) {
            ParseResult result = parser.parseFile(file);
            filesProcessed++;
            invalidLines += result.getInvalidLines();
            allEntries.addAll(result.getEntries());

            if (result.getError() != null) {
                System.err.println("Error reading file " + file + ": " + result.getError().getMessage());
            }
        }

        if (allEntries.isEmpty()) {
            System.err.println("No valid log entries parsed.");
            return;
        }

        SecurityAnalyzer analyzer = new SecurityAnalyzer();
        AnalysisResult analysis = analyzer.analyze(allEntries);

        BruteForceDetector detector = new BruteForceDetector(threshold, Duration.ofMinutes(windowMinutes));
        List<BruteForceFinding> findings = detector.analyze(allEntries);

        List<IpStats> statsList = new ArrayList<>(analysis.getStatsByIp().values());
        statsList.sort(Comparator.comparingInt(IpStats::getTotalEvents).reversed().thenComparing(IpStats::getIp));

        Summary summary = new Summary(
            analysis.getTotalEvents(),
            analysis.getSuccessEvents(),
            analysis.getFailEvents(),
            invalidLines,
            filesProcessed
        );

        ReportData reportData = new ReportData(statsList, findings, summary);

        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            System.err.println("Failed to create output directory: " + e.getMessage());
            return;
        }

        List<ReportGenerator> generators = List.of(
            new TxtReportGenerator(),
            new CsvReportGenerator(),
            new JsonReportGenerator()
        );

        for (ReportGenerator generator : generators) {
            try {
                generator.generate(reportData, outputDir);
            } catch (IOException e) {
                System.err.println("Failed to generate report: " + e.getMessage());
            }
        }

        printSummary(summary, findings, statsList, outputDir);
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-h":
                case "--help":
                    map.put("help", "true");
                    break;
                case "-i":
                case "--input":
                    if (i + 1 < args.length) {
                        map.put("input", args[++i]);
                    }
                    break;
                case "-o":
                case "--out":
                    if (i + 1 < args.length) {
                        map.put("output", args[++i]);
                    }
                    break;
                case "-t":
                case "--threshold":
                    if (i + 1 < args.length) {
                        map.put("threshold", args[++i]);
                    }
                    break;
                case "-w":
                case "--window":
                    if (i + 1 < args.length) {
                        map.put("window", args[++i]);
                    }
                    break;
                default:
                    if (arg.startsWith("--")) {
                        // ignore unknown long args
                    }
                    break;
            }
        }
        return map;
    }

    private static int parseIntOrDefault(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -jar target/mini-siem-1.0.0.jar -i <input> -o <output> -t <threshold> -w <windowMinutes>");
        System.out.println("\nOptions:");
        System.out.println("  -i, --input       Log file or directory containing logs");
        System.out.println("  -o, --out         Output directory for reports (default: reports)");
        System.out.println("  -t, --threshold   Brute-force threshold (default: 5)");
        System.out.println("  -w, --window      Time window in minutes (default: 10)");
        System.out.println("  -h, --help        Show this help message");
    }

    private static void printSummary(Summary summary, List<BruteForceFinding> findings, List<IpStats> statsList, Path outputDir) {
        System.out.println("\n=== Summary ===");
        System.out.println("Files processed: " + summary.getFilesProcessed());
        System.out.println("Total events: " + summary.getTotalEvents());
        System.out.println("Success events: " + summary.getSuccessEvents());
        System.out.println("Fail events: " + summary.getFailEvents());
        System.out.println("Invalid lines: " + summary.getInvalidLines());

        System.out.println("\nBrute-force suspects: " + findings.size());
        for (BruteForceFinding finding : findings) {
            System.out.println("- " + finding.getIp() + " (max fails: " + finding.getMaxFailsInWindow() + ")");
        }

        System.out.println("\nTop IPs by activity:");
        int limit = Math.min(5, statsList.size());
        for (int i = 0; i < limit; i++) {
            IpStats stats = statsList.get(i);
            System.out.println("- " + stats.getIp() + " (events: " + stats.getTotalEvents() + ")");
        }

        System.out.println("\nReports generated in: " + outputDir.toAbsolutePath());
    }
}
