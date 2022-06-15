package edu.kpi.lab05.server.process;

import edu.kpi.lab05.common.FileTransferUtils;
import edu.kpi.lab05.server.context.AppContext;
import edu.kpi.lab05.server.model.file.UploadStatus;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import static edu.kpi.lab05.server.context.AppContext.FILE_STORAGE_URL;

public class FileUploadProcess extends Thread {

    private final Socket socket;
    private final BufferedReader reader;

    public FileUploadProcess(final Socket socket, final BufferedReader reader) {

        this.socket = socket;
        this.reader = reader;
    }

    @SneakyThrows
    @Override
    public void run() {

        final String uid = readNextLine();

        final var file = AppContext.FILE_REPOSITORY.getFileByUid(uid).orElse(null);

        if (file != null) {

            file.setUploadStatus(UploadStatus.UPLOADING);

            FileTransferUtils.receiveFile(FILE_STORAGE_URL + file.getUid(), reader);

            file.setUploadStatus(UploadStatus.AVAILABLE);
        }

        closeSocket();
    }

    private String readNextLine() {

        try {

            return reader.readLine();

        } catch (final IOException e) {

            return null;
        }
    }

    private void closeSocket() {

        try {

            socket.close();

        } catch (final IOException e) {

            // ignore exception
        }
    }

    @SneakyThrows
    private int read(final char[] buffer, final long sizeToRead) {

        return reader.read(buffer, 0, getIterationReadSize(buffer.length, sizeToRead));
    }

    private int getIterationReadSize(final int bufferLength, final long sizeToRead) {

        return (int) Math.min(bufferLength, sizeToRead);
    }
}
