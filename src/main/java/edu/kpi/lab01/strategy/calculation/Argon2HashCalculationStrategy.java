package edu.kpi.lab01.strategy.calculation;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2HashCalculationStrategy {

    private static final int ITERATIONS = 1;
    private static final int MEMORY_COST = 195312; // in kibibytes ~ 200MB
    private static final int PARALLELISM = 1;

    private final Argon2 argon2;

    public Argon2HashCalculationStrategy() {

        this.argon2 = Argon2Factory.create();
    }

    public String calculate(final String password) {

        return argon2.hash(ITERATIONS, MEMORY_COST, PARALLELISM, password.toCharArray());
    }
}
