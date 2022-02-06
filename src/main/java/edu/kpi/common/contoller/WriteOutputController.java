package edu.kpi.common.contoller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteOutputController {

    public static void writeToFile(final String path, final String result) {

        try (final var writer = new BufferedWriter(new FileWriter(path))) {

            writer.append(result);

        } catch (final IOException e) {

            e.printStackTrace();
        }
    }
}