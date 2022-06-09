package edu.kpi.lab03.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DisplayUtils {

    public static void displayCrystal(final AtomicInteger[][] crystal, final int K, final int iteration) {

        System.out.println("\nIteration #" + iteration);
        DisplayUtils.displayCrystal(crystal, K);
    }

    public static void displayCrystal(final AtomicInteger[][] crystal, final int K) {

        final int maxNumberLength = String.valueOf(K).length();

        final var horizontalDivider = createHorizontalDivider(crystal.length, maxNumberLength);

        final String map = Arrays.stream(crystal)
                .map(integerLine -> Arrays.stream(integerLine)
                        .map(v -> DisplayUtils.getIntString(v, maxNumberLength))
                        .collect(Collectors.joining(" ")))
                .map(line -> "|" + line + "|")
                .collect(Collectors.joining("\n"));

        System.out.println(horizontalDivider + map + "\n" + horizontalDivider);
    }

    private static String createHorizontalDivider(final int crystalLength, final int maxNumberLength) {

        return " " + IntStream.range(0, crystalLength)
                .mapToObj(i -> "-".repeat(maxNumberLength))
                .collect(Collectors.joining(" ")) + " \n";
    }

    private static String getIntString(final AtomicInteger integer, final int maxNumberLength) {

        return Optional.of(integer.get())
                .filter(v -> v > 0)
                .map(v -> String.format("%" + maxNumberLength + "d", v))
                .orElse(" ".repeat(maxNumberLength));
    }
}
