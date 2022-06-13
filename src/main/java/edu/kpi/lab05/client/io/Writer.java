package edu.kpi.lab05.client.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Writer extends Thread {

    private final Socket socket;
    private final PrintWriter writer;
    private final Scanner scanner;
    private final String name;

    public Writer(final Socket socket, final Scanner scanner, final String name) throws IOException {

        this.socket = socket;
        this.scanner = scanner;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.name = name;
    }

    @Override
    public void run() {

        writer.println("client");
        writer.println(name);

        String command = scanner.nextLine();

        while (command != null && !command.equals("exit")) {

            cleanUpInput();
            writer.println(command);

            command = scanner.nextLine();
        }
    }

    private void cleanUpInput() {

        System.out.printf("\033[%dA", 1);
        System.out.print("\033[2K");
    }
}
