package edu.kpi.lab02.utils;

import edu.kpi.lab02.generator.Generator;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class MatrixGenerationUtils {

    public static <T> T[][] generateMatrix(final int firstDimension, final int secondDimension,
                                           final BiFunction<Integer, Integer, T[][]> resultInstantiator,
                                           final Generator<T> generator) {

        final T[][] result = resultInstantiator.apply(firstDimension, secondDimension);

        IntStream.range(0, firstDimension)
                .forEach(firstIndex -> IntStream.range(0, secondDimension)
                        .forEach(secondIndex -> result[firstIndex][secondIndex] = generator.generate()));

        return result;
    }
}
