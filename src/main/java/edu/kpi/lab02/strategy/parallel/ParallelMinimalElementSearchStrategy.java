package edu.kpi.lab02.strategy.parallel;

import edu.kpi.lab02.data.Pair;
import edu.kpi.lab02.strategy.MinimalElementSearchStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelMinimalElementSearchStrategy<T extends Number> implements MinimalElementSearchStrategy<T> {

    private final int threadQuantity;
    private final Comparator<T> comparator;

    private static class Task extends Thread {

        private final Supplier<Pair> action;
        public Pair result;

        public Task(final Supplier<Pair> action) {

            this.action = action;
        }

        @Override
        public void run() {

            result = action.get();
        }

        public Pair getResult() {

            return result;
        }
    }

    public ParallelMinimalElementSearchStrategy(final int threadQuantity, final Comparator<T> comparator) {

        this.threadQuantity = threadQuantity;
        this.comparator = comparator;
    }

    @Override
    public Pair find(final T[][] matrix) {

        List<List<Pair>> threadBuckets = createThreadBuckets(matrix);

        final List<Task> threads = threadBuckets.stream()
                .map(bucket -> new Task(() -> findMinimalElementInBucket(matrix, bucket)))
                .collect(Collectors.toList());

        for (final Thread thread : threads) thread.start();
        for (final Thread thread : threads) joinThread(thread);

        return threads.stream()
                .map(Task::getResult)
                .min((pair1, pair2) -> comparator.compare(matrix[pair1.getFirstIndex()][pair1.getSecondIndex()],
                        matrix[pair2.getFirstIndex()][pair2.getSecondIndex()]))
                .orElseThrow(() -> new IllegalArgumentException("Empty result!"));
    }

    private List<Pair> getCoordinatePairs(final T[][] matrix) {

        return IntStream.range(0, matrix.length)
                .boxed()
                .flatMap(firstIndex -> IntStream.range(0, matrix[0].length)
                        .mapToObj(secondIndex -> new Pair(firstIndex, secondIndex)))
                .collect(Collectors.toList());
    }

    private List<List<Pair>> createThreadBuckets(final T[][] matrix) {

        final List<Pair> coordinates = getCoordinatePairs(matrix);

        return IntStream.range(0, coordinates.size())
                .boxed()
                .collect(Collectors.groupingBy(index -> index % threadQuantity))
                .values()
                .stream()
                .map(integers -> integers.stream().map(coordinates::get).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private Pair findMinimalElementInBucket(final T[][] matrix, final List<Pair> bucket) {

        return bucket.stream()
                .min((pair1, pair2) -> comparator.compare(matrix[pair1.getFirstIndex()][pair1.getSecondIndex()],
                        matrix[pair2.getFirstIndex()][pair2.getSecondIndex()]))
                .orElseThrow(() -> new IllegalArgumentException("Empty bucket!"));
    }

    private void joinThread(final Thread thread) {

        try {

            thread.join();

        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    }
}
