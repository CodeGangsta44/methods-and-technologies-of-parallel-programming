package edu.kpi.lab05.server.chat;

import edu.kpi.lab05.server.model.file.File;
import edu.kpi.lab05.server.model.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public abstract class AbstractChat {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Set<User> connectedUsers;

    private Set<User> users;
    private Map<User, List<String>> unreadMessages = new HashMap<>();
    private List<File> files = new ArrayList<>();

    protected AbstractChat(final Set<User> users, final Set<User> connectedUsers) {

        this.users = users;
        this.connectedUsers = connectedUsers;
    }

    public abstract String getConnectionGreeting(final User user);

    protected void createUnreadMessage(final User receiver, final String message) {

        Optional.ofNullable(unreadMessages.get(receiver))
                .ifPresentOrElse(messages -> messages.add(message),
                        () -> unreadMessages.put(receiver, new ArrayList<>(Collections.singletonList(message))));
    }

    public synchronized void sendMessage(final User sender, final String message) {

        sender.getPrintWriter().println("[You|" + LocalDateTime.now().format(formatter) + "]: " + message);

        final var messageToSend = "[" + sender.getUsername() + "|" + LocalDateTime.now().format(formatter) + "]: " + message;

        connectedUsers.stream()
                .filter(u -> !u.equals(sender))
                .forEach(user -> user.getPrintWriter().println(messageToSend));

        users.stream()
                .filter(Predicate.not(connectedUsers::contains))
                .forEach(receiver -> createUnreadMessage(receiver, messageToSend));
    }

    public synchronized void connectUser(final User user) {

        connectedUsers.forEach(u -> u.getPrintWriter().println(user.getUsername() + getNewConnectionPostfix()));
        connectedUsers.add(user);

        Optional.ofNullable(unreadMessages.remove(user))
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresent(messages -> {
                    user.getPrintWriter().println("Here are messages that you missed:");
                    messages.forEach(message -> user.getPrintWriter().println(message));
                });
    }

    public synchronized void disconnectUser(final User user) {

        connectedUsers.remove(user);

        connectedUsers.forEach(u -> u.getPrintWriter().println(user.getUsername() + getDisconnectionPostfix()));
    }

    protected abstract String getNewConnectionPostfix();

    protected abstract String getDisconnectionPostfix();

    public Set<User> getUsers() {

        return users;
    }

    protected Set<User> getConnectedUsers() {

        return connectedUsers;
    }

    public synchronized void addFile(final File file) {

        files.add(file);
    }

    public List<File> getFiles() {

        return new ArrayList<>(files);
    }

    public void setUsers(final Set<User> users) {

        this.users = users;
    }

    public void setUnreadMessages(final Map<User, List<String>> unreadMessages) {

        this.unreadMessages = unreadMessages;
    }

    public void setFiles(final List<File> files) {

        this.files = files;
    }

    public Map<User, List<String>> getUnreadMessages() {

        return unreadMessages;
    }
}
