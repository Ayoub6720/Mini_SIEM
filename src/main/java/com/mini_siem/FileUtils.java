package com.mini_siem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    private FileUtils() {
    }

    public static List<Path> collectLogFiles(Path inputPath) throws IOException {
        List<Path> files = new ArrayList<>();
        if (Files.isDirectory(inputPath)) {
            try (Stream<Path> stream = Files.walk(inputPath)) {
                files = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".log") || p.toString().endsWith(".txt"))
                    .collect(Collectors.toList());
            }
        } else if (Files.isRegularFile(inputPath)) {
            files.add(inputPath);
        }
        return files;
    }
}
