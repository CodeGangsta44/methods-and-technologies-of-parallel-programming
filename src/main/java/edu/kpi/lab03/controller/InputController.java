package edu.kpi.lab03.controller;

import java.util.Scanner;

public class InputController {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static int readInt(final String message, final int lowerLimit, final int upperLimit) {

        System.out.print(message);

        int value = SCANNER.nextInt();

        while (!isValid(value, lowerLimit, upperLimit)) {

            System.out.println("Entered value is not correct!\nPlease, try again");
            System.out.print(message);
            value = SCANNER.nextInt();
        }

        return value;
    }

    public static double readDouble(final String message, final double lowerLimit, final double upperLimit) {

        System.out.print(message);

        double value = SCANNER.nextDouble();

        while (!isValid(value, lowerLimit, upperLimit)) {

            System.out.println("Entered value is not correct!\nPlease, try again");
            System.out.print(message);
            value = SCANNER.nextDouble();
        }

        return value;
    }

    private static boolean isValid(final int value, final int lowerLimit, final int upperLimit) {

        return value >= lowerLimit && value <= upperLimit;
    }

    private static boolean isValid(final double value, final double lowerLimit, final double upperLimit) {

        return value >= lowerLimit && value <= upperLimit;
    }
}
