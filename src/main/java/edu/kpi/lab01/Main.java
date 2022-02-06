package edu.kpi.lab01;

import edu.kpi.lab01.strategy.analyzation.AnalyzationStrategy;
import edu.kpi.lab01.strategy.calculation.PiCalculationStrategy;

public class Main {

    public static void main(final String... args) {

        new AnalyzationStrategy(32,
                () -> new PiCalculationStrategy(3).execute(),
                "./results/lab01/cpu-bound/results.csv")
                .execute();
    }
}
