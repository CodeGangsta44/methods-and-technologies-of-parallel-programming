package edu.kpi.lab01.strategy.analyzation;

import edu.kpi.common.contoller.WriteOutputController;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static edu.kpi.common.constants.Constants.Configuration.WARM_UP_THREADS;
import static edu.kpi.common.constants.Constants.Messages.POWER_OF_TWO_MESSAGE;
import static edu.kpi.common.constants.Constants.Output.DURATION_FORMAT;
import static edu.kpi.common.constants.Constants.Output.RESULT_HEADER;

public class AnalyzationStrategy {

    private final int maxThreads;
    private final int iterations;
    private final Runnable action;
    private final String resultFilePath;

    public AnalyzationStrategy(final int maxThreads, final int iterations, final Runnable action, final String resultFilePath) {

        if ((maxThreads & maxThreads - 1) != 0) {

            throw new IllegalArgumentException(POWER_OF_TWO_MESSAGE);
        }

        this.maxThreads = maxThreads;
        this.iterations = iterations;
        this.action = action;
        this.resultFilePath = resultFilePath;
    }

    public void execute() {

        final var result = new StringBuilder(RESULT_HEADER);

        IntStream.range(0, (int) (Math.log(maxThreads) / Math.log(2)) + 1)
                .map(power -> (int) Math.pow(2, power))
                .mapToObj(this::executeForNumberOfThreads)
                .forEach(result::append);

        WriteOutputController.writeToFile(resultFilePath, result.toString());
    }

    private String executeForNumberOfThreads(final int numberOfThreads) {

        warmUpAnalyzationCode();

        long serialExecutionTime = executeInSingleThread(numberOfThreads);
        long parallelExecutionTime = executeInParallel(numberOfThreads);

        final double accelerationCoefficient = (double) serialExecutionTime / parallelExecutionTime;
        final double efficiencyCoefficient = accelerationCoefficient / numberOfThreads;

        return numberOfThreads + ";"
                + serialExecutionTime + ";"
                + convertDuration(serialExecutionTime) + ';'
                + parallelExecutionTime + ";"
                + convertDuration(parallelExecutionTime) + ';'
                + accelerationCoefficient + ";"
                + efficiencyCoefficient + "\n";
    }

    private long executeInSingleThread(final int numberOfTasks) {

        return executeTest(() -> performSerialRun(numberOfTasks));
    }

    private long executeInParallel(final int numberOfTasks) {

        return executeTest(() -> performParallelRun(numberOfTasks));
    }

    private long executeTest(final Runnable task) {

        task.run();

        long start = System.nanoTime();

        for (var i = 0; i < iterations; i++) task.run();

        long finish = System.nanoTime();

        return (finish - start) / iterations;
    }

    private void performSerialRun(final int numberOfTasks) {

        IntStream.range(0, numberOfTasks)
                .forEach(number -> action.run());
    }

    private void performParallelRun(final int numberOfTasks) {

        final Thread[] threads = IntStream.range(0, numberOfTasks)
                .mapToObj(number -> new Thread(action))
                .toArray(Thread[]::new);

        for (final Thread thread : threads) thread.start();
        for (final Thread thread : threads) joinThread(thread);
    }

    private void joinThread(final Thread thread) {

        try {

            thread.join();

        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    }

    private void warmUpAnalyzationCode() {

        executeInSingleThread(WARM_UP_THREADS);
        executeInParallel(WARM_UP_THREADS);
    }

    private String convertDuration(final long duration) {

        final long seconds = TimeUnit.NANOSECONDS.toSeconds(duration);
        final long milliseconds = TimeUnit.NANOSECONDS.toMillis(duration) - TimeUnit.SECONDS.toMillis(seconds);

        return String.format(DURATION_FORMAT, seconds, milliseconds);
    }
}
