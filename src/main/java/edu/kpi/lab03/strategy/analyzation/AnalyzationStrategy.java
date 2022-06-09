package edu.kpi.lab03.strategy.analyzation;

import edu.kpi.common.contoller.WriteOutputController;
import edu.kpi.lab03.particle.Particle;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnalyzationStrategy {

    private static final double PROBABILITY_STEP = 0.05;
    private static final int N = 40;
    private static final int M = 40;
    private static final int K = 20;
    private static final int ITERATIONS = 30_000;
    private static final int TEST_ITERATIONS = 10;

    public void execute() {

        final var resultFilePath = "./results/lab03/results.csv";

        warmUp();

        final var builder = new StringBuilder("#;" + getHeader() + "\n");

        IntStream.range(0, (int) (1.0 / PROBABILITY_STEP) + 1)
                .boxed()
                .map(index -> index * PROBABILITY_STEP)
                .map(this::executeForX)
                .peek(System.out::println)
                .forEach(builder::append);

        WriteOutputController.writeToFile(resultFilePath, builder.toString());
    }

    private String executeForX(final double xProbability) {

        return String.format("%.2f", xProbability) + ";" + IntStream.range(0, (int) (1.0 / PROBABILITY_STEP) + 1)
                .boxed()
                .map(index -> index * PROBABILITY_STEP)
                .map(yProbability -> execute(xProbability, yProbability))
                .map(duration -> String.format("%.2f", duration / 1000_000.0))
                .collect(Collectors.joining(";")) + "\n";
    }

    @SneakyThrows
    private long execute(final double xProbability, final double yProbability) {

        var totalDuration = 0;

        for (var i = 0; i < TEST_ITERATIONS; i++) totalDuration += executeIteration(xProbability, yProbability);

        return totalDuration / TEST_ITERATIONS;
    }

    @SneakyThrows
    private long executeIteration(final double xProbability, final double yProbability) {

        final var crystal = IntStream.range(0, N)
                .mapToObj(x -> IntStream.range(0, M)
                        .mapToObj(y -> new AtomicInteger())
                        .toArray(AtomicInteger[]::new))
                .toArray(AtomicInteger[][]::new);

        crystal[0][0].addAndGet(K);

        final var particles = IntStream.range(0, K)
                .boxed()
                .map(i -> new Particle(crystal, xProbability, yProbability, ITERATIONS, null))
                .toArray(Particle[]::new);

        final var startTime = System.nanoTime();

        for (final var particle : particles) particle.start();
        for (final var particle : particles) particle.join();

        return System.nanoTime() - startTime;
    }

    private String getHeader() {

        return IntStream.range(0, (int) (1.0 / PROBABILITY_STEP) + 1)
                .boxed()
                .map(index -> String.format("%.2f", index * PROBABILITY_STEP))
                .collect(Collectors.joining(";"));
    }

    private void warmUp() {

        execute(0.5, 0.5);
    }
}
