package edu.kpi.lab02.strategy.analyzation;

import edu.kpi.common.contoller.WriteOutputController;
import edu.kpi.lab02.generator.impl.RandomIntegerGenerator;
import edu.kpi.lab02.strategy.MatrixMultiplicationStrategy;
import edu.kpi.lab02.strategy.MinimalElementSearchStrategy;
import edu.kpi.lab02.utils.MatrixGenerationUtils;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static edu.kpi.common.constants.Constants.Configuration.PARALLEL_WARM_UP_THREADS;
import static edu.kpi.common.constants.Constants.Configuration.SERIAL_WARM_UP_THREADS;
import static edu.kpi.common.constants.Constants.Configuration.WARM_UP_SIZE;
import static edu.kpi.common.constants.Constants.Messages.POWER_OF_TWO_MESSAGE;

public class AnalyzationStrategy {

    private final int maxThreads;
    private final int iterations;
    private final String resultFilePath;
    private final int minSize;
    private final int maxSize;
    private final int stepSize;

    private final IntFunction<MatrixMultiplicationStrategy<Integer>> parallelMultiplicationStrategy;
    private final IntFunction<MinimalElementSearchStrategy<Integer>> parallelMinimalElementSearchStrategy;
    private final Supplier<MatrixMultiplicationStrategy<Integer>> serialMultiplicationStrategy;
    private final Supplier<MinimalElementSearchStrategy<Integer>> serialMinimalElementSearchStrategy;

    public AnalyzationStrategy(final int maxThreads,
                               final int iterations,
                               final String resultFilePath,
                               final int minSize,
                               final int maxSize,
                               final int stepSize,
                               final IntFunction<MatrixMultiplicationStrategy<Integer>> parallelMultiplicationStrategy,
                               final IntFunction<MinimalElementSearchStrategy<Integer>> parallelMinimalElementSearchStrategy,
                               final Supplier<MatrixMultiplicationStrategy<Integer>> serialMultiplicationStrategy,
                               final Supplier<MinimalElementSearchStrategy<Integer>> serialMinimalElementSearchStrategy) {

        if ((maxThreads & maxThreads - 1) != 0) {

            throw new IllegalArgumentException(POWER_OF_TWO_MESSAGE);
        }

        this.maxThreads = maxThreads;
        this.iterations = iterations;
        this.resultFilePath = resultFilePath;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.stepSize = stepSize;
        this.parallelMultiplicationStrategy = parallelMultiplicationStrategy;
        this.parallelMinimalElementSearchStrategy = parallelMinimalElementSearchStrategy;
        this.serialMultiplicationStrategy = serialMultiplicationStrategy;
        this.serialMinimalElementSearchStrategy = serialMinimalElementSearchStrategy;
    }

    public void execute() {

        warmUp();

        final var builder = new StringBuilder("#;" + getHeader() + "\n");

        IntStream.range(0, (maxSize - minSize) / stepSize)
                .boxed()
                .map(iteration -> minSize + (iteration * stepSize))
                .map(this::executeForSize)
                .peek(System.out::print)
                .forEach(builder::append);

        WriteOutputController.writeToFile(resultFilePath, builder.toString());
    }

    private String getHeader() {

        return getThreadQuantities()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }

    private String executeForSize(final int size) {

        return size + ";" + getThreadQuantities()
                .map(threadQty -> executeForNumberOfThreadsAndSize(threadQty, size))
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }

    private long executeForNumberOfThreadsAndSize(final int threadQty, final int size) {

        return execute(getStrategy(serialMultiplicationStrategy, parallelMultiplicationStrategy, threadQty),
                getStrategy(serialMinimalElementSearchStrategy, parallelMinimalElementSearchStrategy, threadQty),
                createMatrix(size),
                createMatrix(size));
    }

    private Integer[][] createMatrix(final int size) {

        return MatrixGenerationUtils.generateMatrix(size, size, (d1, d2) -> new Integer[d1][d2], new RandomIntegerGenerator());
    }

    private <T> T getStrategy(final Supplier<T> serialSupplier, final IntFunction<T> parallelSupplier, final int threadQty) {

        return Optional.of(threadQty)
                .filter(qty -> qty > 1)
                .map(parallelSupplier::apply)
                .orElseGet(serialSupplier);
    }

    private long execute(final MatrixMultiplicationStrategy<Integer> multiplicationStrategy,
                         final MinimalElementSearchStrategy<Integer> minimalElementSearchStrategy,
                         final Integer[][] matrix1, final Integer[][] matrix2) {

        long start = System.nanoTime();

        for (var i = 0; i < iterations; i++) {

            minimalElementSearchStrategy.find(multiplicationStrategy
                    .multiply(matrix1, matrix2));
        }

        return (System.nanoTime() - start) / iterations;
    }

    private Stream<Integer> getThreadQuantities() {

        return IntStream.range(0, (int) (Math.log(maxThreads) / Math.log(2)) + 1)
                .boxed()
                .map(power -> (int) Math.pow(2, power));
    }

    private void warmUp() {

        executeForNumberOfThreadsAndSize(SERIAL_WARM_UP_THREADS, WARM_UP_SIZE);
        executeForNumberOfThreadsAndSize(PARALLEL_WARM_UP_THREADS, WARM_UP_SIZE);
    }
}
