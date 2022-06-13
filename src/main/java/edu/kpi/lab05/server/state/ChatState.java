package edu.kpi.lab05.server.state;

import edu.kpi.lab05.server.chat.AbstractChat;
import edu.kpi.lab05.server.context.AppContext;
import edu.kpi.lab05.server.model.file.File;
import edu.kpi.lab05.server.model.file.UploadStatus;
import edu.kpi.lab05.server.model.user.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChatState extends AbstractState {

    private final Map<String, Function<String, UserState>> actionMap = Map.ofEntries(
            Map.entry("/file-upload", this::uploadFile),
            Map.entry("/file-download", this::downloadFile),
            Map.entry("/list-files", this::listFiles));

    private final AbstractChat chat;
    private final AbstractState previousState;

    public ChatState(final User user, final AbstractChat chat, final AbstractState previousState) {

        super(user);
        this.chat = chat;
        this.previousState = previousState;

        commonMessage();
        chat.connectUser(getUser());
    }

    @Override
    protected Map<String, Function<String, UserState>> getActionMap() {

        return actionMap;
    }

    @Override
    protected UserState defaultAction(final String command) {

        chat.sendMessage(getUser(), command);
        return this;
    }

    @Override
    protected AbstractState getPreviousState() {

        chat.disconnectUser(getUser());
        return previousState;
    }

    @Override
    protected void commonMessage() {

        sendMessage(chat.getConnectionGreeting(getUser()));
    }

    private UserState uploadFile(final String command) {

        final String[] arguments = command.split(" ");
        final String[] filePath = arguments[1].split("/");
        final String fileName = filePath[filePath.length - 1];

        final var file = File.builder()
                .name(fileName)
                .uid(UUID.randomUUID().toString())
                .uploadStatus(UploadStatus.WAITING)
                .build();

        chat.addFile(file);
        AppContext.FILE_REPOSITORY.addFile(file);

        sendMessage("CONTROL " + file.getUid() + " " + command);
        return this;
    }

    private UserState downloadFile(final String command) {

        final String[] arguments = command.split(" ");
        final String fileUid = arguments[1];

        AppContext.FILE_REPOSITORY.getFileByUid(fileUid)
                .ifPresentOrElse(file -> sendMessage("CONTROL " + file.getName() + " " + command),
                        () -> sendMessage("No such file in chat."));

        return this;
    }

    private UserState listFiles(final String command) {

        Optional.of(chat.getFiles())
                .filter(Predicate.not(Collection::isEmpty))
                .ifPresentOrElse(list -> {
                    sendMessage("List of files in chat:");
                    list.forEach(file -> sendMessage(file.getName() + " - " + file.getUid() + " - [" + file.getUploadStatus() + "]"));
                }, () -> sendMessage("Currently there are no files in the chat."));

        return this;
    }
}
