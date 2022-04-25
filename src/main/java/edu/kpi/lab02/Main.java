package edu.kpi.lab02;

import edu.kpi.lab02.strategy.analyzation.AnalyzationStrategy;
import edu.kpi.lab02.strategy.parallel.ParallelMatrixMultiplicationStrategy;
import edu.kpi.lab02.strategy.parallel.ParallelMinimalElementSearchStrategy;
import edu.kpi.lab02.strategy.serial.SerialMatrixMultiplicationStrategy;
import edu.kpi.lab02.strategy.serial.SerialMinimalElementSearchStrategy;

public class Main {

    public static void main(final String... args) {

        new AnalyzationStrategy(128,
                10,
                "./results/lab02/results.csv",
                100,
                1000,
                100,
                threadQty -> new ParallelMatrixMultiplicationStrategy<>(threadQty,
                        Integer::sum,
                        (i1, i2) -> i1 * i2,
                        (d1, d2) -> new Integer[d1][d2],
                        () -> 0),
                threadQty -> new ParallelMinimalElementSearchStrategy<>(threadQty, Integer::compareTo),
                () -> new SerialMatrixMultiplicationStrategy<>(Integer::sum,
                        (i1, i2) -> i1 * i2,
                        (d1, d2) -> new Integer[d1][d2],
                        () -> 0),
                () -> new SerialMinimalElementSearchStrategy<>(Integer::compareTo))
        .execute();
    }
}