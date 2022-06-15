package edu.kpi.lab05.server.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kpi.lab05.server.chat.GroupChat;
import edu.kpi.lab05.server.chat.PersonalChat;
import edu.kpi.lab05.server.context.AppContext;
import edu.kpi.lab05.server.model.user.Status;
import edu.kpi.lab05.server.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.kpi.common.constants.Constants.DataSource.FILES;
import static edu.kpi.common.constants.Constants.DataSource.GROUP_CHATS;
import static edu.kpi.common.constants.Constants.DataSource.PERSONAL_CHATS;
import static edu.kpi.common.constants.Constants.DataSource.USERS;

public class Persistence {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String dataSourceUrl;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class UserDto {

        private String username;
        private String password;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class GroupChatDto {

        private String name;
        private String password;
        private Set<String> users;
        private Map<String, List<String>> unreadMessages;
        private List<String> files;
        private String creator;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class PersonalChatDto {

        private Set<String> users;
        private Map<String, List<String>> unreadMessages;
        private List<String> files;
    }

    public Persistence(final String dataSourceUrl) {

        this.dataSourceUrl = dataSourceUrl;
    }

    public void init() {

        try {

            MAPPER.readValue(new File(dataSourceUrl + FILES),
                    new TypeReference<List<edu.kpi.lab05.server.model.file.File>>() {
                    })
                    .forEach(AppContext.FILE_REPOSITORY::addFile);

            MAPPER.readValue(new File(dataSourceUrl + USERS),
                    new TypeReference<List<UserDto>>() {
                    })
                    .stream().map(this::createUser)
                    .forEach(AppContext.USER_REPOSITORY::addUser);

            MAPPER.readValue(new File(dataSourceUrl + GROUP_CHATS),
                    new TypeReference<List<GroupChatDto>>() {
                    })
                    .stream().map(this::createGroupChat)
                    .forEach(AppContext.GROUP_CHAT_REPOSITORY::addChat);

            MAPPER.readValue(new File(dataSourceUrl + PERSONAL_CHATS),
                    new TypeReference<List<PersonalChatDto>>() {
                    })
                    .stream().map(this::createPersonalChat)
                    .forEach(AppContext.PERSONAL_CHAT_REPOSITORY::addChat);

        } catch (final Exception e) {

            e.printStackTrace();
        }

    }

    @SneakyThrows
    public void save() {

        MAPPER.writeValue(new File(dataSourceUrl + FILES),
                AppContext.FILE_REPOSITORY.getFiles());

        MAPPER.writeValue(new File(dataSourceUrl + USERS),
                AppContext.USER_REPOSITORY.getAllUsers().stream()
                        .map(user -> UserDto.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .build())
                        .collect(Collectors.toList()));

        MAPPER.writeValue(new File(dataSourceUrl + GROUP_CHATS),
                AppContext.GROUP_CHAT_REPOSITORY.getAll().stream()
                        .map(this::createGroupChatDto)
                        .collect(Collectors.toList()));

        MAPPER.writeValue(new File(dataSourceUrl + PERSONAL_CHATS),
                AppContext.PERSONAL_CHAT_REPOSITORY.getAll().stream()
                        .map(this::createPersonalChatDto)
                        .collect(Collectors.toList()));
    }

    private User createUser(final UserDto user) {

        return User.builder()
                .username(user.username)
                .password(user.password)
                .status(Status.OFFLINE)
                .build();
    }

    private GroupChat createGroupChat(final GroupChatDto chat) {

        final var creator = AppContext.USER_REPOSITORY.getByUsername(chat.creator);
        final var groupChat = new GroupChat(creator.orElse(null), chat.name, chat.password);

        groupChat.setUsers(getUsers(chat.users));
        groupChat.setFiles(getFiles(chat.files));
        groupChat.setUnreadMessages(getUnreadMessages(chat.unreadMessages));

        return groupChat;
    }

    private PersonalChat createPersonalChat(final PersonalChatDto chat) {

        final var users = new ArrayList<>(getUsers(chat.users));

        final var personalChat = new PersonalChat(users.get(0), users.get(1));

        personalChat.setFiles(getFiles(chat.files));
        personalChat.setUnreadMessages(getUnreadMessages(chat.unreadMessages));

        return personalChat;
    }

    private Set<User> getUsers(final Set<String> users) {

        return users.stream()
                .map(AppContext.USER_REPOSITORY::getByUsername)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private List<edu.kpi.lab05.server.model.file.File> getFiles(final List<String> files) {

        return files.stream()
                .map(AppContext.FILE_REPOSITORY::getFileByUid)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Map<User, List<String>> getUnreadMessages(final Map<String, List<String>> unreadMessages) {

        return new HashMap<>(unreadMessages.entrySet()
                .stream()
                .map(entry -> Map.entry(AppContext.USER_REPOSITORY.getByUsername(entry.getKey()).orElseThrow(IllegalStateException::new), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private GroupChatDto createGroupChatDto(final GroupChat chat) {

        return GroupChatDto.builder()
                .name(chat.getName())
                .password(chat.getPassword())
                .creator(chat.getCreator().getUsername())
                .files(chat.getFiles().stream().map(edu.kpi.lab05.server.model.file.File::getUid).collect(Collectors.toList()))
                .users(chat.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()))
                .unreadMessages(chat.getUnreadMessages().entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey().getUsername(), entry.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .build();
    }

    private PersonalChatDto createPersonalChatDto(final PersonalChat chat) {

        return PersonalChatDto.builder()
                .files(chat.getFiles().stream().map(edu.kpi.lab05.server.model.file.File::getUid).collect(Collectors.toList()))
                .users(chat.getUsers().stream().map(User::getUsername).collect(Collectors.toSet()))
                .unreadMessages(chat.getUnreadMessages().entrySet().stream()
                        .map(entry -> Map.entry(entry.getKey().getUsername(), entry.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .build();
    }
}
