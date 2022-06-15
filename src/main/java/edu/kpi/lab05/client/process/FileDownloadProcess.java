package edu.kpi.lab05.client.process;

import edu.kpi.lab05.client.context.AppContext;
import edu.kpi.lab05.common.FileTransferUtils;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FileDownloadProcess extends Thread {

    private final String fileName;
    private final String uid;
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    @SneakyThrows
    public FileDownloadProcess(final String host, final int port, final String fileName, final String uid) {

        this.fileName = fileName;
        this.uid = uid;
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {

        writer.println("file-download");
        writer.println(uid);

        FileTransferUtils.receiveFile(AppContext.getFileStorageUrl() + fileName, reader);

        closeSocket();
    }

    private void closeSocket() {

        try {

            socket.close();

        } catch (final IOException e) {

            // ignore exception
        }
    }
}
