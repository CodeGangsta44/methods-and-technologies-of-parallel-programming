package edu.kpi.lab05.server.model.user;

import edu.kpi.lab05.server.chat.GroupChat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private String username;
    private String password;
    private Socket socket;
    private Status status;

    private final List<GroupChat> groupChats = new ArrayList<>();

    public PrintWriter getPrintWriter() {

        try {

            return new PrintWriter(socket.getOutputStream(), true);

        } catch (final IOException e) {

            throw new IllegalStateException(e);
        }
    }
}
