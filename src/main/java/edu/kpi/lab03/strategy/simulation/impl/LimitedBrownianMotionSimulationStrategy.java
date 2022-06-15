package edu.kpi.lab03.strategy.simulation.impl;

import edu.kpi.common.utils.DurationUtils;
import edu.kpi.lab03.parameter.LimitedBrownianMotionSimulationParameter;
import edu.kpi.lab03.particle.Particle;
import edu.kpi.lab03.strategy.simulation.AbstractSimulationStrategy;
import edu.kpi.lab03.strategy.simulation.BrownianMotionSimulationStrategy;
import edu.kpi.lab03.utils.DisplayUtils;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitedBrownianMotionSimulationStrategy extends AbstractSimulationStrategy implements BrownianMotionSimulationStrategy<LimitedBrownianMotionSimulationParameter> {

    @Override
    public void simulate(final LimitedBrownianMotionSimulationParameter parameter) {

        final AtomicInteger[][] crystal = createCrystal(parameter.getN(), parameter.getM());

        crystal[0][0].addAndGet(parameter.getK());

        final var phaser = new Phaser(1);

        final var particles = createThreads(parameter.getK(),
                () -> new Particle(crystal, parameter.getXProbability(), parameter.getYProbability(), parameter.getIterations(), phaser));

        final long duration = executeSimulation(parameter, particles, crystal, phaser);

        System.out.println("\nFinal crystal state:");
        DisplayUtils.displayCrystal(crystal, parameter.getK());
        System.out.println("Total execution time: " + DurationUtils.convertDuration(duration));
    }

    private long executeSimulation(final LimitedBrownianMotionSimulationParameter parameter,
                                   final Particle[] particles, final AtomicInteger[][] crystal,
                                   final Phaser phaser) {

        long startTime = System.nanoTime();

        final var displayThread = new Watcher(phaser, crystal, particles, parameter.getK(), parameter.getInterval());

        startThreads(particles);
        displayThread.start();

        joinThreads(particles);
        displayThread.interrupt();

        return System.nanoTime() - (startTime + displayThread.getAggregatedPauseDuration());
    }

    private static class Watcher extends Thread {

        private final Phaser phaser;
        private final AtomicInteger[][] crystal;
        private final Particle[] particles;
        private final int k;
        private final int interval;

        private long aggregatedPauseDuration = 0;

        public Watcher(final Phaser phaser, final AtomicInteger[][] crystal, final Particle[] particles, final int k, final int interval) {
            this.phaser = phaser;
            this.crystal = crystal;
            this.particles = particles;
            this.k = k;
            this.interval = interval;
        }

        @Override
        public void run() {

            while (!isInterrupted()) {

                for (var particle : particles) particle.setWaitForPhase(Boolean.TRUE);
                phaser.arriveAndAwaitAdvance();

                var startPause = System.nanoTime();

                System.out.println("\nIntermediate state:");
                DisplayUtils.displayCrystal(crystal, k);

                for (var particle : particles) particle.setWaitForPhase(Boolean.FALSE);

                aggregatedPauseDuration += System.nanoTime() - startPause;
                phaser.arriveAndAwaitAdvance();

                sleep(interval);
            }
        }

        private void sleep(final int interval) {

            try {

                Thread.sleep(interval);

            } catch (final InterruptedException e) {

                interrupt();
            }
        }

        public long getAggregatedPauseDuration() {

            return aggregatedPauseDuration;
        }
    }
}