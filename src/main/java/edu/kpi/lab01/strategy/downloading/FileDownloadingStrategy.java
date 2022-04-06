package edu.kpi.lab01.strategy.downloading;

import java.io.InputStream;
import java.net.URL;

public class FileDownloadingStrategy {

    public void download(final String url) {

        try (InputStream in = new URL(url).openStream()) {

            in.readAllBytes();

        } catch (final Exception e) {

            e.printStackTrace();
        }
    }
}
