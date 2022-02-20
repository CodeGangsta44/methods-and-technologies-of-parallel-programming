package edu.kpi.lab01.strategy.copying;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileCopyingStrategy {

    public void copy(final String inputPath, final String outputPath) {

        try {

            Files.copy(Path.of(inputPath), Path.of(outputPath), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
