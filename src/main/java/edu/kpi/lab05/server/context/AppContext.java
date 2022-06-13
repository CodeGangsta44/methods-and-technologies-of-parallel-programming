package edu.kpi.lab05.server.context;

import edu.kpi.lab05.server.repository.FileRepository;
import edu.kpi.lab05.server.repository.GroupChatRepository;
import edu.kpi.lab05.server.repository.PersonalChatRepository;
import edu.kpi.lab05.server.repository.UserRepository;

public class AppContext {

    public static final UserRepository USER_REPOSITORY = new UserRepository();
    public static final GroupChatRepository GROUP_CHAT_REPOSITORY = new GroupChatRepository();
    public static final PersonalChatRepository PERSONAL_CHAT_REPOSITORY = new PersonalChatRepository();
    public static final FileRepository FILE_REPOSITORY = new FileRepository();

    public static final String FILE_STORAGE_URL = "/home/rd/Documents/training/methods-and-technologies-of-parallel-programming/sandbox/server/";

    private AppContext() {}

    private static String fileStorageUrl;

    public static String getFileStorageUrl() {

        return fileStorageUrl;
    }

    public static void setFileStorageUrl(final String fileStorageUrl) {

        if (AppContext.fileStorageUrl == null) {

            AppContext.fileStorageUrl = fileStorageUrl;
        }
    }
}
