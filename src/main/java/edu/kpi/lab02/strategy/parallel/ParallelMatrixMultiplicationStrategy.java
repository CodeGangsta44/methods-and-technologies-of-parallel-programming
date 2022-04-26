package edu.kpi.lab02.strategy.parallel;

import edu.kpi.lab02.strategy.MatrixMultiplicationStrategy;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ParallelMatrixMultiplicationStrategy<T extends Number> implements MatrixMultiplicationStrategy<T> {

    private final int threadQuantity;
    private final BinaryOperator<T> addFunction;
    private final BinaryOperator<T> multiplyFunction;
    private final Supplier<T> zeroInstantiator;

    public ParallelMatrixMultiplicationStrategy(final int threadQuantity, final BinaryOperator<T> addFunction,
                                                final BinaryOperator<T> multiplyFunction,
                                                final Supplier<T> zeroInstantiator) {

        this.threadQuantity = threadQuantity;
        this.addFunction = addFunction;
        this.multiplyFunction = multiplyFunction;
        this.zeroInstantiator = zeroInstantiator;
    }

    @Override
    public void multiply(final T[][] firstMatrix, final T[][] secondMatrix, final T[][] result) {

        validateMatrices(firstMatrix, secondMatrix);

        multiplyMatrices(firstMatrix, secondMatrix, result);
    }

    private void validateMatrices(final T[][] firstMatrix, final T[][] secondMatrix) {

        if (firstMatrix[0].length != secondMatrix.length) {

            throw new IllegalArgumentException("Inner dimensions are different!");
        }
    }

    private void multiplyMatrices(final T[][] firstMatrix, final T[][] secondMatrix, final T[][] result) {

        final Thread[] threads = IntStream.range(0, threadQuantity)
                .boxed()
                .map(index -> new Thread(() -> multiply(firstMatrix, secondMatrix, result, index)))
                .toArray(Thread[]::new);

        for (final Thread thread : threads) thread.start();
        for (final Thread thread : threads) joinThread(thread);
    }

    private void multiply(final T[][] firstMatrix, final T[][] secondMatrix, final T[][] result, final int currentThread) {

        int length = result.length;
        int width = result[0].length;
        int bound = length * width;

        for (int i = currentThread; i < bound; i += threadQuantity) {

            calculateCellValue(firstMatrix, secondMatrix, result, i / width, i % width);
        }
    }

    private void calculateCellValue(final T[][] firstMatrix, final T[][] secondMatrix, final T[][] result, final int firstIndex, final int secondIndex) {

        result[firstIndex][secondIndex] = IntStream.range(0, firstMatrix[0].length)
                .mapToObj(index -> multiplyFunction.apply(firstMatrix[firstIndex][index],
                        secondMatrix[index][secondIndex]))
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
