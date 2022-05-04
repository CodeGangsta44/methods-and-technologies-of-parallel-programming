package edu.kpi.common.constants;

public class Constants {

    public static class Parameters {

        public static final String FILE_DOWNLOAD_URL = "file.download.url";
    }

    public static class Configuration {

        public static final int WARM_UP_THREADS = 1;
        public static final int SERIAL_WARM_UP_THREADS = 1;
        public static final int PARALLEL_WARM_UP_THREADS = 2;

        public static final int WARM_UP_SIZE = 1000;
    }

    public static class Output {

        public static final String RESULT_HEADER = "numberOfThreads;serialExecutionTime(ns);serialExecutionTime;parallelExecutionTime(ns);parallelExecutionTime;accelerationCoefficient;efficiencyCoefficient\n";
        public static final String DURATION_FORMAT = "%ds %dms";
    }

    public static class Messages {

        public static final String POWER_OF_TWO_MESSAGE = "Max threads argument should be power of two";
    }
}
