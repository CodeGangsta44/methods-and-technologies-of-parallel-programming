package edu.kpi.lab05.server.state;

import edu.kpi.lab05.server.chat.GroupChat;
import edu.kpi.lab05.server.context.AppContext;
import edu.kpi.lab05.server.model.user.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GroupMenuState extends AbstractState {

    private final Map<String, Function<String, UserState>> actionMap = Map.ofEntries(
            Map.entry("/create", this::createGroupChat),
            Map.entry("/join", this::joinGroupChat),
            Map.entry("/list", this::listGroupChats),
            Map.entry("/connect", this::connectToGroupChat),
            Map.entry("/leave", this::leaveGroupChat));

    private final AbstractState previousState;

    public GroupMenuState(final User user, final AbstractState previousState) {

        super(user);
        this.previousState = previousState;
        commonMessage();
    }

    @Override
    protected Map<String, Function<String, UserState>> getActionMap() {

        return actionMap;
    }

    @Override
    protected AbstractState getPreviousState() {

        return previousState;
    }

    @Override
    protected void commonMessage() {

        sendMessage("You are in group chat menu. Available actions: [/create {name} {password}, /join {name} {password}, /list, /connect {name}, /leave {name}]");
    }

    private UserState createGroupChat(final String command) {

        final String[] arguments = command.split(" ");
        final String chatName = arguments[1];
        final String password = arguments[2];

        final var chat = new GroupChat(getUser(), chatName, password);

        AppContext.GROUP_CHAT_REPOSITORY.addChat(chat);
        getUser().getGroupChats().add(chat);

        sendMessage("Successfully created chat [" + chatName + "].");

        return this;
    }

    private UserState joinGroupChat(final String command) {

        final String[] arguments = command.split(" ");
        final String chatName = arguments[1];
        final String password = arguments[2];

        AppContext.GROUP_CHAT_REPOSITORY.getByName(chatName)
                .filter(chat -> chat.matchesPassword(password))
                .ifPresentOrElse(chat -> {
                            sendMessage("Successfully joined chat [" + chatName + "].");
                            chat.addUser(getUser());
                        },
                        () -> sendMessage("There is no chat with such credentials. Please check your data and try again."));


        return this;
    }

    private UserState listGroupChats(final String command) {

        final String chatListMessage = Optional.of(getUser().getGroupChats())
                .filter(Predicate.not(Collection::isEmpty))
                .map(list -> "List of your group chats: " + list.stream()
                        .map(GroupChat::getName)
                        .collect(Collectors.joining(", ")))
                .orElse("Currently you don`t have any group chats.");

        sendMessage(chatListMessage);

        return this;
    }

    private UserState connectToGroupChat(final String command) {

        final String[] arguments = command.split(" ");
        final String chatName = arguments[1];

        return getUser().getGroupChats().stream()
                .filter(chat -> chatName.equals(chat.getName()))
                .findAny()
                .map(chat -> new ChatState(getUser(), chat, this))
                .map(AbstractState.class::cast)
                .orElseGet(() -> {
                    sendMessage("There is no chat with such name. Please check your data and try again.");
                    return this;
                });
    }

    private UserState leaveGroupChat(final String command) {

        final String[] arguments = command.split(" ");
        final String chatName = arguments[1];

        getUser().getGroupChats().stream()
                .filter(chat -> chatName.equals(chat.getName()))
                .findAny()
                .ifPresentOrElse(chat -> chat.removeUser(getUser()),
                        () -> sendMessage("There is no chat with such name. Please check your data and try again."));

        return this;
    }
}
