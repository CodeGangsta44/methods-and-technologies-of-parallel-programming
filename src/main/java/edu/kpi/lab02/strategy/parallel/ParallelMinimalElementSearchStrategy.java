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

        final List<Task> threads = IntStream.range(0, threadQuantity)
                .boxed()
                .map(index -> new Task(() -> findMinimalElement(matrix, index)))
                .collect(Collectors.toList());

        for (final Thread thread : threads) thread.start();
        for (final Thread thread : threads) joinThread(thread);

        return threads.stream()
                .map(Task::getResult)
                .min((pair1, pair2) -> comparator.compare(matrix[pair1.getFirstIndex()][pair1.getSecondIndex()],
                        matrix[pair2.getFirstIndex()][pair2.getSecondIndex()]))
                .orElseThrow(() -> new IllegalArgumentException("Empty result!"));
    }

    private Pair findMinimalElement(final T[][] matrix, final int currentThread) {

        int length = matrix.length;
        int width = matrix[0].length;
        int bound = length * width;

        var minimalX = 0;
        var minimalY = 0;
        var currentMinimal = matrix[minimalX][minimalY];

        for (int i = currentThread; i < bound; i += threadQuantity) {

            if (comparator.compare(matrix[i / width][i % width], currentMinimal) < 0) {

                minimalX = i / width;
                minimalY = i % width;
                currentMinimal = matrix[i / width][i % width];
            }
        }

        return new Pair(minimalX, minimalY);
    }

    private void joinThread(final Thread thread) {

        try {

            thread.join();

        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    }
}
