package edu.kpi.lab05.server.state;

import edu.kpi.lab05.server.model.user.User;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractState implements UserState {

    private final User user;

    public AbstractState(final User user) {

        this.user = user;
    }

    @Override
    public UserState executeCommand(final String command) {

        return getActionMap().entrySet()
                .stream()
                .filter(entry -> command.startsWith(entry.getKey()))
                .findAny()
                .map(Map.Entry::getValue)
                .map(action -> action.apply(command))
                .orElseGet(() -> executeUnrecognizedAction(command));
    }

    protected abstract Map<String, Function<String, UserState>> getActionMap();

    protected abstract AbstractState getPreviousState();

    protected abstract void commonMessage();

    private UserState executeUnrecognizedAction(final String command) {

        return Optional.ofNullable(command)
                .filter(c -> c.startsWith("/back"))
                .map(c -> {
                    final AbstractState previousState = getPreviousState();
                    previousState.commonMessage();
                    return previousState;
                })
                .map(UserState.class::cast)
                .orElseGet(() -> defaultAction(command));
    }

    protected UserState defaultAction(final String command) {

        sendMessage("Unrecognized input");
        commonMessage();
        return this;
    }

    protected void sendMessage(final String message) {

        user.getPrintWriter().println(message);
    }

    protected User getUser() {

        return user;
    }
}
