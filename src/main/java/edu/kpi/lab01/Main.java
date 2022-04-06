package edu.kpi.lab01;

import edu.kpi.lab01.strategy.allocation.ArrayAllocationStrategy;
import edu.kpi.lab01.strategy.analyzation.AnalyzationStrategy;
import edu.kpi.lab01.strategy.calculation.Argon2HashCalculationStrategy;
import edu.kpi.lab01.strategy.calculation.PiCalculationStrategy;
import edu.kpi.lab01.strategy.copying.FileCopyingStrategy;
import edu.kpi.lab01.strategy.downloading.FileDownloadingStrategy;

import java.util.Random;

import static edu.kpi.common.constants.Constants.Parameters.FILE_DOWNLOAD_URL;

public class Main {

    public static void main(final String... args) {

        final var piCalculationStrategy = new PiCalculationStrategy(3);

        new AnalyzationStrategy(128, 10,
                piCalculationStrategy::calculate,
                "./results/lab01/cpu-bound/results.csv")
                .execute();


        final var argon2HashCalculationStrategy = new Argon2HashCalculationStrategy();
        final var password = "password";

        new AnalyzationStrategy(128, 10,
                () -> argon2HashCalculationStrategy.calculate(password),
                "./results/lab01/hashing/results.csv")
                .execute();


        final var allocationStrategy = new ArrayAllocationStrategy(400_000_000); // 400MB

        new AnalyzationStrategy(128, 10,
                allocationStrategy::execute,
                "./results/lab01/memory-bound/results.csv")
                .execute();


        final var copyingStrategy = new FileCopyingStrategy();

        new AnalyzationStrategy(128, 1,
                () -> copyingStrategy.copy("./input/E.coli", "./sandbox/output/copy-" + new Random().nextInt(100000)),
                "./results/lab01/io-bound/disk/results.csv")
                .execute();


        final var downloadingStrategy = new FileDownloadingStrategy();
        final var fileDownloadUrl = System.getenv(FILE_DOWNLOAD_URL);

        new AnalyzationStrategy(128, 10,
                () -> downloadingStrategy.download(fileDownloadUrl),
                "./results/lab01/io-bound/network/results.csv")
                .execute();
    }
}
