package edu.kpi.lab02.strategy.parallel;

import edu.kpi.lab02.data.Pair;
import edu.kpi.lab02.strategy.MatrixMultiplicationStrategy;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelMatrixMultiplicationStrategy<T extends Number> implements MatrixMultiplicationStrategy<T> {

    private final int threadQuantity;
    private final BinaryOperator<T> addFunction;
    private final BinaryOperator<T> multiplyFunction;
    private final Supplier<T> zeroInstantiator;
    private final BiFunction<Integer, Integer, T[][]> resultInstantiator;

    public ParallelMatrixMultiplicationStrategy(final int threadQuantity, final BinaryOperator<T> addFunction,
                                                final BinaryOperator<T> multiplyFunction, final BiFunction<Integer, Integer, T[][]> resultInstantiator,
                                                final Supplier<T> zeroInstantiator) {

        this.threadQuantity = threadQuantity;
        this.addFunction = addFunction;
        this.multiplyFunction = multiplyFunction;
        this.resultInstantiator = resultInstantiator;
        this.zeroInstantiator = zeroInstantiator;
    }

    @Override
    public T[][] multiply(final T[][] firstMatrix, final T[][] secondMatrix) {

        validateMatrices(firstMatrix, secondMatrix);

        final T[][] result = resultInstantiator.apply(firstMatrix.length, secondMatrix[0].length);

        multiplyMatrices(firstMatrix, secondMatrix, result);

        return result;
    }

    private void validateMatrices(final T[][] firstMatrix, final T[][] secondMatrix) {

        if (firstMatrix[0].length != secondMatrix.length) {

            throw new IllegalArgumentException("Inner dimensions are different!");
        }
    }

    private void multiplyMatrices(final T[][] firstMatrix, final T[][] secondMatrix, final T[][] result) {

        final List<List<Pair>> threadBuckets = createThreadBuckets(firstMatrix, secondMatrix);

        final Thread[] threads = threadBuckets.stream()
                .map(bucket -> new Thread(() -> multiplyForBucket(firstMatrix, secondMatrix, result, bucket)))
                .toArray(Thread[]::new);

        for (final Thread thread : threads) thread.start();
        for (final Thread thread : threads) joinThread(thread);
    }

    private List<Pair> getCoordinatePairs(final T[][] firstMatrix, final T[][] secondMatrix) {

        return IntStream.range(0, firstMatrix.length)
                .boxed()
                .flatMap(firstIndex -> IntStream.range(0, secondMatrix[0].length)
                        .mapToObj(secondIndex -> new Pair(firstIndex, secondIndex)))
                .collect(Collectors.toList());
    }

    private List<List<Pair>> createThreadBuckets(final T[][] firstMatrix, final T[][] secondMatrix) {

        final List<Pair> coordinates = getCoordinatePairs(firstMatrix, secondMatrix);

        return IntStream.range(0, coordinates.size())
                .boxed()
                .collect(Collectors.groupingBy(index -> index % threadQuantity))
                .values()
                .stream()
                .map(integers -> integers.stream().map(coordinates::get).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private void multiplyForBucket(final T[][] firstMatrix, final T[][] secondMatrix, final T[][] result, final List<Pair> bucket) {

        bucket.forEach(pair -> result[pair.getFirstIndex()][pair.getSecondIndex()] = calculateCellValue(firstMatrix, secondMatrix, pair));
    }

    private T calculateCellValue(final T[][] firstMatrix, final T[][] secondMatrix, final Pair coordinatePair) {

        return IntStream.range(0, firstMatrix[0].length)
                .mapToObj(index -> multiplyFunction.apply(firstMatrix[coordinatePair.getFirstIndex()][index],
                        secondMatrix[index][coordinatePair.getSecondIndex()]))
                .reduce(zeroInstantiator.get(), addFunction);
    }

    private void joinThread(final Thread thread) {

        try {

            thread.join();

        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    }
}
