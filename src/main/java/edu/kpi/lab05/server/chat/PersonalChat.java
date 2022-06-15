package edu.kpi.lab05.server.chat;

import edu.kpi.lab05.server.model.user.User;

import java.util.HashSet;
import java.util.Set;

public class PersonalChat extends AbstractChat {

    public PersonalChat(final User firstUser, final User secondUser) {

        super(new HashSet<>(Set.of(firstUser, secondUser)), new HashSet<>());
    }

    @Override
    public String getConnectionGreeting(final User user) {

        return "Connected to chat with [" + getAnotherUser(user).getUsername() + "]";
    }

    @Override
    protected String getNewConnectionPostfix() {

        return " just connected to private chat...";
    }

    @Override
    protected String getDisconnectionPostfix() {

        return " just disconnected from private chat...";
    }

    public boolean checkUsers(final User firstUser, final User secondUser) {

        return getUsers().contains(firstUser) && getUsers().contains(secondUser);
    }

    public User getAnotherUser(final User user) {

        return getUsers().stream()
                .filter(u -> !user.equals(u))
                .findAny()
                .orElseThrow(IllegalStateException::new);
    }

    public boolean belongsToUser(final User user) {

        return getUsers().contains(user);
    }
}
