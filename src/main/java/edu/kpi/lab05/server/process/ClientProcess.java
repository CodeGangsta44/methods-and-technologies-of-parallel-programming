package edu.kpi.lab05.server.process;

import edu.kpi.lab05.server.context.AppContext;
import edu.kpi.lab05.server.model.user.Status;
import edu.kpi.lab05.server.model.user.User;
import edu.kpi.lab05.server.state.RootState;
import edu.kpi.lab05.server.state.UserState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess extends Thread {

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private User user;
    private UserState state;

    public ClientProcess(final Socket socket, final BufferedReader reader) {

        try {

            this.socket = socket;
            this.reader = reader;
            this.writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (final IOException e) {

            throw new IllegalStateException(e);
        }
    }

    @Override
    public void run() {

        authorize();

        if (user != null) {

            var message = readNextLine();

            while (message != null) {

                this.state = state.executeCommand(message);
                message = readNextLine();
            }

            finishSession();
        }
    }

    private void authorize() {

        String message = readNextLine();

        while (user == null && message != null) {
            final String[] line = message.split(" ");
            final String username = line[0];
            final String password = line[1];

            AppContext.USER_REPOSITORY.getByUsername(username)
                    .ifPresentOrElse(user -> {
                        if (user.getPassword().equals(password)) {

                            user.setSocket(socket);
                            user.setStatus(Status.ONLINE);
                            this.user = user;
                            this.state = new RootState(user);

                        } else {
                            writeMessage("Entered credentials are invalid. Please, try again.");
                        }
                    }, () -> createUser(username, password));

            if (user == null) {

                message = readNextLine();
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

    private void writeMessage(final String message) {

        writer.println(message);
    }

    private void createUser(final String username, final String password) {

        final var user = User.builder()
                .username(username)
                .password(password)
                .socket(socket)
                .status(Status.ONLINE)
                .build();

        AppContext.USER_REPOSITORY.addUser(user);
        this.user = user;
        this.state = new RootState(user);
    }

    private void finishSession() {

        user.getGroupChats()
                .forEach(chat -> chat.disconnectUser(user));

        AppContext.PERSONAL_CHAT_REPOSITORY.getAllForUser(user)
                .forEach(chat -> chat.disconnectUser(user));

        user.setStatus(Status.OFFLINE);
    }
}
