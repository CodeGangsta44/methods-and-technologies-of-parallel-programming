package edu.kpi.lab05.client.context;

public class AppContext {

    private static String host;
    private static int port;
    private static String fileStorageUrl;

    private AppContext(){}

    public static String getHost() {

        return host;
    }

    public static void setHost(final String host) {

        if (AppContext.host == null) {

            AppContext.host = host;
        }
    }

    public static int getPort() {

        return port;
    }

    public static void setPort(final int port) {

        if (AppContext.port == 0) {

            AppContext.port = port;
        }
    }

    public static String getFileStorageUrl() {

        return fileStorageUrl;
    }

    public static void setFileStorageUrl(final String fileStorageUrl) {

        if (AppContext.fileStorageUrl == null) {

            AppContext.fileStorageUrl = fileStorageUrl;
        }
    }
}
