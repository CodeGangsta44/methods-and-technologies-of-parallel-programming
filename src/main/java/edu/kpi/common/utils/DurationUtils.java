package edu.kpi.common.utils;

import java.util.concurrent.TimeUnit;

import static edu.kpi.common.constants.Constants.Output.DURATION_FORMAT;

public class DurationUtils {

    public static String convertDuration(final long duration) {

        final long seconds = TimeUnit.NANOSECONDS.toSeconds(duration);
        final long milliseconds = TimeUnit.NANOSECONDS.toMillis(duration) - TimeUnit.SECONDS.toMillis(seconds);

        return String.format(DURATION_FORMAT, seconds, milliseconds);
    }
}
