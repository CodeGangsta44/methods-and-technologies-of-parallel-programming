package edu.kpi.lab05.server.repository;

import edu.kpi.lab05.server.chat.PersonalChat;
import edu.kpi.lab05.server.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonalChatRepository {

    private final List<PersonalChat> chats = new ArrayList<>();

    public PersonalChat getOrCreateForUsers(final User firstUser, final User secondUser) {

        return chats.stream()
                .filter(chat -> chat.checkUsers(firstUser, secondUser))
                .findAny()
                .orElseGet(() -> {
                    final var chat = new PersonalChat(firstUser, secondUser);
                    chats.add(chat);
                    return chat;
                });
    }

    public List<PersonalChat> getAllForUser(final User user) {

        return chats.stream()
                .filter(chat -> chat.belongsToUser(user))
                .collect(Collectors.toList());
    }

    public void addChat(final PersonalChat chat) {

        chats.add(chat);
    }

    public List<PersonalChat> getAll() {

        return chats;
    }
}
