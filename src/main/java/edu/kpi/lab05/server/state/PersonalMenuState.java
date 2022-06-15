package edu.kpi.lab05.server.state;

import edu.kpi.lab05.server.context.AppContext;
import edu.kpi.lab05.server.model.user.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PersonalMenuState extends AbstractState {

    private final Map<String, Function<String, UserState>> actionMap = Map.ofEntries(
            Map.entry("/list", this::listUsers),
            Map.entry("/connect", this::connectToPersonalChat));

    private final AbstractState previousState;

    public PersonalMenuState(final User user, final AbstractState previousState) {

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

        sendMessage("You are in personal chat menu. Available actions: [/list, /connect {username}]");
    }

    private UserState listUsers(final String command) {

        Optional.of(AppContext.USER_REPOSITORY.getAllUsers().stream()
                .filter(u -> !getUser().getUsername().equals(u.getUsername()))
                .collect(Collectors.toList()))
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresentOrElse(list -> {
                    sendMessage("List of users in app:");
                    list.forEach(u -> sendMessage(u.getUsername() + "[" + u.getStatus() + "]"));
                }, () -> sendMessage("Currently there are no users in the app."));

        return this;
    }

    private UserState connectToPersonalChat(final String command) {

        final String[] arguments = command.split(" ");
        final String username = arguments[1];

        return AppContext.USER_REPOSITORY.getByUsername(username)
                .map(user -> AppContext.PERSONAL_CHAT_REPOSITORY.getOrCreateForUsers(getUser(), user))
                .map(chat -> new ChatState(getUser(), chat, this))
                .map(UserState.class::cast)
                .orElseGet(() -> {
                    sendMessage("There is no user with such username. Please, check your data and try again.");
                    return this;
                });
    }
}
