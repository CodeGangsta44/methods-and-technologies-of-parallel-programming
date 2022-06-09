package edu.kpi.lab03.particle;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Particle extends Thread {

    private final AtomicInteger[][] crystal;
    private final int xLimit;
    private final int yLimit;
    private final double xProbability;
    private final double yProbability;
    private final Random random = new Random();
    private final int iterations;
    private final Phaser phaser;
    private int x = 0;
    private int y = 0;
    private int iterationCount = 0;

    public Particle(final AtomicInteger[][] crystal, final double xProbability, final double yProbability, final Phaser phaser) {

        this(crystal, xProbability, yProbability, -1, phaser);
        phaser.register();
    }

    public Particle(final AtomicInteger[][] crystal, final double xProbability, final double yProbability, final int iterations) {

        this(crystal, xProbability, yProbability, iterations, null);
    }

    public Particle(final AtomicInteger[][] crystal, final double xProbability, final double yProbability, final int iterations, final Phaser phaser) {

        this.crystal = crystal;
        xLimit = crystal.length;
        yLimit = crystal[0].length;
        this.xProbability = xProbability;
        this.yProbability = yProbability;
        this.iterations = iterations;
        this.phaser = phaser;
    }

    @Override
    public void run() {

        while (!isInterrupted() && hasToIterate()) {

            final int newX = getNewValue(x, xLimit, xProbability);
            final int newY = getNewValue(y, yLimit, yProbability);

            crystal[x][y].decrementAndGet();
            crystal[newX][newY].incrementAndGet();

            x = newX;
            y = newY;

            iterationCount++;

            waitIfNeeded();
        }

        deregisterIfNeeded();
    }

    private int getNewValue(final int currentValue, final int limit, final double probability) {

        return getSeed() < probability ? movePositive(currentValue, limit) : moveNegative(currentValue);
    }

    private double getSeed() {

        return random.nextInt(100) / 100.0;
    }

    private int movePositive(final int currentValue, final int limit) {

        return (currentValue + 1) < limit ? currentValue + 1 : currentValue - 1;
    }

    private int moveNegative(final int currentValue) {

        return (currentValue - 1) >= 0 ? currentValue - 1 : currentValue + 1;
    }

    private boolean hasToIterate() {

        return iterations < 0 || iterationCount < iterations;
    }

    private void waitIfNeeded() {

        Optional.ofNullable(phaser)
                .ifPresent(p -> {
                    p.arriveAndAwaitAdvance();
                    p.arriveAndAwaitAdvance();
                });
    }

    private void deregisterIfNeeded() {

        Optional.ofNullable(phaser)
                .ifPresent(Phaser::arriveAndDeregister);
    }
}
