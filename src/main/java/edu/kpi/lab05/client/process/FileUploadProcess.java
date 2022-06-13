package edu.kpi.lab05.client.process;

import edu.kpi.lab05.common.FileTransferUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class FileUploadProcess extends Thread {

    private final String host;
    private final int port;
    private final String uid;
    private final String path;

    public FileUploadProcess(final String host, final int port, final String uid, final String path) {

        this.host = host;
        this.port = port;
        this.uid = uid;
        this.path = path;
    }

    @Override
    public void run() {

        try (final var socket = new Socket(host, port)) {

            final var out = new DataOutputStream(socket.getOutputStream());
            final var writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("file-upload");
            writer.println(uid);

            FileTransferUtils.sendFile(path, writer, out);

            out.close();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
