package edu.kpi.lab05.server;

import edu.kpi.lab05.server.context.AppContext;
import edu.kpi.lab05.server.init.Persistence;
import edu.kpi.lab05.server.process.ClientProcess;
import edu.kpi.lab05.server.process.FileDownloadProcess;
import edu.kpi.lab05.server.process.FileUploadProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.function.BiFunction;

public class Main {

    private static final Map<String, BiFunction<Socket, BufferedReader, Thread>> OPERATION_MAPPING =
            Map.ofEntries(
                    Map.entry("client", ClientProcess::new),
                    Map.entry("file-upload", FileUploadProcess::new),
                    Map.entry("file-download", FileDownloadProcess::new));

    public static void main(final String... args) throws IOException {

        AppContext.setFileStorageUrl(args[1]);

        if (args.length > 2) {

            final var persistence = new Persistence(args[2]);
            persistence.init();

            Runtime.getRuntime().addShutdownHook(new Thread(persistence::save));
        }

        try (final var serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {

            while (!serverSocket.isClosed()) {

                final var socket = serverSocket.accept();
                final var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                OPERATION_MAPPING.get(reader.readLine()).apply(socket, reader).start();
            }
        }
    }
}
