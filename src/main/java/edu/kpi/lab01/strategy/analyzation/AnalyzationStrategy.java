package edu.kpi.lab01.strategy.analyzation;

import edu.kpi.common.contoller.WriteOutputController;

import java.util.stream.IntStream;

public class AnalyzationStrategy {

    public static final String RESULT_HEADER = "numberOfThreads;serialExecutionTime;parallelExecutionTime;accelerationCoefficient;efficiencyCoefficient\n";
    private static final String POWER_OF_TWO_MESSAGE = "Max threads argument should be power of two";

    private final int maxThreads;
    private final Runnable action;
    private final String resultFilePath;

    public AnalyzationStrategy(final int maxThreads, final Runnable action, final String resultFilePath) {

        if ((maxThreads & maxThreads - 1) != 0) {

            throw new IllegalArgumentException(POWER_OF_TWO_MESSAGE);
        }

        this.maxThreads = maxThreads;
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

        long serialExecutionTime = executeInSingleThread(numberOfThreads);
        long parallelExecutionTime = executeInParallel(numberOfThreads);

        final double accelerationCoefficient = (double) serialExecutionTime / parallelExecutionTime;
        final double efficiencyCoefficient = accelerationCoefficient / numberOfThreads;

        return numberOfThreads + ";"
                + serialExecutionTime + ";"
                + parallelExecutionTime + ";"
                + accelerationCoefficient + ";"
                + efficiencyCoefficient + "\n";
    }

    private long executeInSingleThread(final int numberOfTasks) {

        long start = System.nanoTime();

        for (var i = 0; i < 10; i++) {

            IntStream.range(0, numberOfTasks)
                    .forEach(number -> action.run());
        }

        long finish = System.nanoTime();

        return (finish - start) / 10;
    }

    private long executeInParallel(final int numberOfTasks) {

        long start = System.nanoTime();

        for (var i = 0; i < 10; i++) {

            final Thread[] threads = IntStream.range(0, numberOfTasks)
                    .mapToObj(number -> new Thread(action))
                    .toArray(Thread[]::new);

            for (final Thread thread : threads) thread.start();
            for (final Thread thread : threads) joinThread(thread);
        }

        long finish = System.nanoTime();

        return (finish - start) / 10;
    }

    private void joinThread(final Thread thread) {

        try {

            thread.join();

        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    }
}
