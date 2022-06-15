package edu.kpi.lab05.server.state;

import edu.kpi.lab05.server.model.user.User;

import java.util.Map;
import java.util.function.Function;

public class RootState extends AbstractState {

    private final Map<String, Function<String, UserState>> actionMap = Map.ofEntries(
            Map.entry("/groups", this::switchToGroupMenu),
            Map.entry("/personal", this::switchToPersonalMenu));

    public RootState(final User user) {

        super(user);
        commonMessage();
    }

    @Override
    protected Map<String, Function<String, UserState>> getActionMap() {

        return actionMap;
    }

    @Override
    protected AbstractState getPreviousState() {

        return this;
    }

    @Override
    protected void commonMessage() {

        sendMessage("You are in root menu. Available actions: [/groups, /personal]");
    }

    private UserState switchToGroupMenu(final String command) {

        return new GroupMenuState(getUser(), this);
    }

    private UserState switchToPersonalMenu(final String command) {

        return new PersonalMenuState(getUser(), this);
    }
}
