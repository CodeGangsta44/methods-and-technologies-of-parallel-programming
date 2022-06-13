package edu.kpi.lab05.client.io;

import edu.kpi.lab05.client.context.AppContext;
import edu.kpi.lab05.client.process.FileDownloadProcess;
import edu.kpi.lab05.client.process.FileUploadProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Reader extends Thread {

    private final Map<String, Consumer<String>> controlMap = Map.ofEntries(
            Map.entry("/file-upload", this::startFileUpload),
            Map.entry("/file-download", this::startFileDownload));

    private final Socket socket;
    private final BufferedReader reader;

    public Reader(final Socket socket) throws IOException {

        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {

        var message = "";

        while (message != null && !isInterrupted()) {

            message = readNextLine();
            Optional.ofNullable(message)
                    .ifPresent(m -> getActionForCommand(m).accept(m));
        }
    }

    private String readNextLine() {

        try {

            return reader.readLine();

        } catch (final IOException e) {

            return null;
        }
    }

    private Consumer<String> getActionForCommand(final String command) {

        return Optional.ofNullable(command)
                .filter(m -> m.startsWith("CONTROL"))
                .map(m -> m.split(" ")[2])
                .map(controlMap::get)
                .orElse(System.out::println);
    }

    private void startFileUpload(final String command) {

        final String[] arguments = command.split(" ");
        final String uid = arguments[1];
        final String path = arguments[3];

        new FileUploadProcess(AppContext.getHost(), AppContext.getPort(), uid, path).start();
    }

    private void startFileDownload(final String command) {

        final String[] arguments = command.split(" ");
        final String fileName = arguments[1];
        final String uid = arguments[3];

        new FileDownloadProcess(AppContext.getHost(), AppContext.getPort(), fileName, uid).start();
    }
}
