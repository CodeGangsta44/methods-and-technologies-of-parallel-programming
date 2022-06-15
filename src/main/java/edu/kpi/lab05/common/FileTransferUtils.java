package edu.kpi.lab05.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class FileTransferUtils {

    private static final int BUFFER_SIZE = 4 * 1024;

    private FileTransferUtils() {}

    public static void sendFile(final String path, final PrintWriter writer, final DataOutputStream out) {

        final var file = new File(path);

        try (final var fileInputStream = new FileInputStream(file)) {

            writer.println(file.length());

            final var buffer = new byte[BUFFER_SIZE];
            int bytes;
            while ((bytes = fileInputStream.read(buffer)) != -1) {

                out.write(buffer, 0, bytes);
                out.flush();
            }
        } catch (final IOException e) {

            e.printStackTrace();
        }
    }

    public static void receiveFile(final String path, final BufferedReader reader) {

        try (final var fileOutputStream = new FileOutputStream(path)) {

            int bytes;
            var sizeToRead = Long.parseLong(reader.readLine());
            final var buffer = new char[BUFFER_SIZE];

            while (sizeToRead > 0 &&
                    (bytes = read(reader, buffer, sizeToRead)) != -1) {

                fileOutputStream.write(new String(buffer).getBytes(), 0, bytes);
                sizeToRead -= bytes;
            }

        } catch (final IOException e) {

            e.printStackTrace();
        }
    }

    private static int read(final BufferedReader reader, final char[] buffer, final long sizeToRead) throws IOException {

        return reader.read(buffer, 0, getIterationReadSize(buffer.length, sizeToRead));
    }

    private static int getIterationReadSize(final int bufferLength, final long sizeToRead) {

        return (int) Math.min(bufferLength, sizeToRead);
    }
}
