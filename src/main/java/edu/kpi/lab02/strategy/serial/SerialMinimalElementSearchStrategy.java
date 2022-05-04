package edu.kpi.lab02.strategy.serial;

import edu.kpi.lab02.data.Pair;
import edu.kpi.lab02.strategy.MinimalElementSearchStrategy;

import java.util.Comparator;
import java.util.stream.IntStream;

public class SerialMinimalElementSearchStrategy<T extends Number> implements MinimalElementSearchStrategy<T> {

    private final Comparator<T> comparator;

    public SerialMinimalElementSearchStrategy(final Comparator<T> comparator) {

        this.comparator = comparator;
    }

    @Override
    public Pair find(final T[][] matrix) {

        return IntStream.range(0, matrix.length)
                .boxed()
                .flatMap(firstIndex -> IntStream.range(0, matrix[0].length).boxed()
                        .map(secondIndex -> new Pair(firstIndex, secondIndex)))
                .min((pair1, pair2) -> comparator.compare(matrix[pair1.getFirstIndex()][pair1.getSecondIndex()],
                        matrix[pair2.getFirstIndex()][pair2.getSecondIndex()]))
                .orElseThrow(() -> new IllegalArgumentException("Empty result!"));
    }
}
