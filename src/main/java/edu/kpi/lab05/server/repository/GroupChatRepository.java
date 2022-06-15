package edu.kpi.lab05.server.repository;

import edu.kpi.lab05.server.chat.GroupChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupChatRepository {

    private final List<GroupChat> chats = new ArrayList<>();

    public Optional<GroupChat> getByName(final String name) {

        return chats.stream()
                .filter(c -> name.equals(c.getName()))
                .findAny();
    }

    public synchronized void addChat(final GroupChat chat) {

        chats.add(chat);
    }

    public List<GroupChat> getAll() {

        return chats;
    }
}
