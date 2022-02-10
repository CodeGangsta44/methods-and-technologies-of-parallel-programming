package edu.kpi.lab01;

import edu.kpi.lab01.strategy.allocation.ArrayAllocationStrategy;
import edu.kpi.lab01.strategy.analyzation.AnalyzationStrategy;
import edu.kpi.lab01.strategy.calculation.Argon2HashCalculationStrategy;
import edu.kpi.lab01.strategy.calculation.PiCalculationStrategy;

public class Main {

    public static void main(final String... args) {

        final var piCalculationStrategy = new PiCalculationStrategy(3);

        new AnalyzationStrategy(128,
                piCalculationStrategy::calculate,
                "./results/lab01/cpu-bound/results.csv")
                .execute();


        final var argon2HashCalculationStrategy = new Argon2HashCalculationStrategy();
        final var password = "password";

        new AnalyzationStrategy(128,
                () -> argon2HashCalculationStrategy.calculate(password),
                "./results/lab01/hashing/results.csv")
                .execute();


        final var allocationStrategy = new ArrayAllocationStrategy(400_000_000); // 400MB

        new AnalyzationStrategy(128,
                allocationStrategy::execute,
                "./results/lab01/memory-bound/results.csv")
                .execute();
    }
}
