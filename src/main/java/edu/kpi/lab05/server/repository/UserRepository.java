package edu.kpi.lab05.server.repository;

import edu.kpi.lab05.server.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public Optional<User> getByUsername(final String username) {

        return users.stream()
                .filter(u -> username.equals(u.getUsername()))
                .findAny();
    }

    public synchronized void addUser(final User user) {

        users.add(user);
    }

    public synchronized List<User> getAllUsers() {

        return new ArrayList<>(users);
    }
}
