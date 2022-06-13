package edu.kpi.lab05.server.chat;

import edu.kpi.lab05.server.model.user.User;

import java.util.Collections;
import java.util.HashSet;

public class GroupChat extends AbstractChat {

    private final String name;
    private final String password;
    private final User creator;

    public GroupChat(final User user, final String name, final String password) {

        super(new HashSet<>(Collections.singletonList(user)), new HashSet<>());

        this.name = name;
        this.password = password;
        this.creator = user;
    }

    @Override
    public String getConnectionGreeting(final User user) {

        return "Connected to group chat [" + getName() + "]";
    }

    @Override
    protected String getNewConnectionPostfix() {

        return " just connected to our chat...";
    }

    @Override
    protected String getDisconnectionPostfix() {

        return " just disconnected from our chat...";
    }

    public synchronized void addUser(final User user) {

        getConnectedUsers().forEach(u -> u.getPrintWriter().println(user.getUsername() + " just joined our chat..."));
        getUsers().add(user);
        user.getGroupChats().add(this);
    }

    public synchronized void removeUser(final User user) {

        getUsers().remove(user);
        user.getGroupChats().remove(this);

        getConnectedUsers().forEach(u -> u.getPrintWriter().println(user.getUsername() + " just left our chat..."));
    }

    public String getName() {

        return name;
    }

    public boolean matchesPassword(final String password) {

        return this.password.equals(password);
    }

    public String getPassword() {

        return password;
    }

    public User getCreator() {

        return creator;
    }
}
