package edu.kpi.lab02.strategy.serial;

import edu.kpi.lab02.strategy.MatrixMultiplicationStrategy;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class SerialMatrixMultiplicationStrategy<T extends Number> implements MatrixMultiplicationStrategy<T> {

    private final BinaryOperator<T> addFunction;
    private final BinaryOperator<T> multiplyFunction;
    private final Supplier<T> zeroInstantiator;

    public SerialMatrixMultiplicationStrategy(final BinaryOperator<T> addFunction,
                                              final BinaryOperator<T> multiplyFunction,
                                              final Supplier<T> zeroInstantiator) {

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

        IntStream.range(0, firstMatrix.length)
                .forEach(firstIndex -> IntStream.range(0, secondMatrix[0].length)
                        .forEach(secondIndex -> result[firstIndex][secondIndex] = calculateCellValue(firstMatrix, secondMatrix, firstIndex, secondIndex)));
    }

    private T calculateCellValue(final T[][] firstMatrix, final T[][] secondMatrix, final int firstIndex, final int secondIndex) {

        return IntStream.range(0, firstMatrix[0].length)
                .mapToObj(index -> multiplyFunction.apply(firstMatrix[firstIndex][index],
                        secondMatrix[index][secondIndex]))
                .reduce(zeroInstantiator.get(), addFunction);
    }
}
