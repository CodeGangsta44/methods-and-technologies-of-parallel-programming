package edu.kpi.lab05.server.state;

public interface UserState {

    UserState executeCommand(final String command);
}
