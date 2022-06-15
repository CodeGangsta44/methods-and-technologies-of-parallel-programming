package edu.kpi.lab05.client;

import edu.kpi.lab05.client.context.AppContext;
import edu.kpi.lab05.client.io.Reader;
import edu.kpi.lab05.client.io.Writer;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(final String... args) throws IOException, InterruptedException {

        AppContext.setHost(args[0]);
        AppContext.setPort(Integer.parseInt(args[1]));
        AppContext.setFileStorageUrl(args[2]);

        final var scanner = new Scanner(System.in);
        System.out.print("Please, enter username and password: ");
        final var name = scanner.nextLine();

        final var socket = new Socket(AppContext.getHost(), AppContext.getPort());

        final var reader = new Reader(socket);
        final var writer = new Writer(socket, scanner, name);

        reader.start();
        writer.start();

        writer.join();
        reader.interrupt();

        socket.close();
    }
}
