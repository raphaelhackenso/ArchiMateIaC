package at.fhjoanneum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ReduceXMLSingleLine {
    public static void main(String[] args) {
        
        String firstFile = args[0];
        String secondFile = args[1];
        String outputFile = args[2];

        try {
            List<String> linesToRemove = readLinesFromFile(firstFile);
            removeLinesFromFile(secondFile, outputFile, linesToRemove);

            System.out.println("Processing completed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readLinesFromFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        return lines;
    }

    private static void removeLinesFromFile(String sourceFile, String outputFile, List<String> linesToRemove)
            throws IOException {
        Path sourcePath = Path.of(sourceFile);
        Path outputPath = Path.of(outputFile);

        List<String> outputLines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(sourcePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!linesToRemove.contains(line.trim())) {
                    outputLines.add(line);
                }
            }
        }

        Files.write(outputPath, outputLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}