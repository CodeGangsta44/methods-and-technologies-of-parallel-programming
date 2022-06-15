package edu.kpi.lab05.server.process;

import edu.kpi.lab05.common.FileTransferUtils;
import edu.kpi.lab05.server.context.AppContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static edu.kpi.lab05.server.context.AppContext.FILE_STORAGE_URL;

public class FileDownloadProcess extends Thread {

    private final Socket socket;
    private final BufferedReader reader;

    public FileDownloadProcess(final Socket socket, final BufferedReader reader) {

        this.socket = socket;
        this.reader = reader;
    }

    @Override
    public void run() {

        final String uid = readNextLine();

        final var file = AppContext.FILE_REPOSITORY.getFileByUid(uid).orElse(null);

        if (file != null) {

            try {

                FileTransferUtils.sendFile(FILE_STORAGE_URL + file.getUid(),
                        new PrintWriter(socket.getOutputStream(), true),
                        new DataOutputStream(socket.getOutputStream()));

            } catch (final Exception e) {

                e.printStackTrace();
            }
        }
    }

    private String readNextLine() {

        try {

            return reader.readLine();

        } catch (final IOException e) {

            return null;
        }
    }
}
