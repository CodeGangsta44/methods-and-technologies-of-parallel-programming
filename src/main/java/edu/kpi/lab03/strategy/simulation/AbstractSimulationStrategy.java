package edu.kpi.lab03.strategy.simulation;

import edu.kpi.lab03.particle.Particle;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class AbstractSimulationStrategy {

    protected Particle[] createThreads(final int K, final Supplier<Particle> instantiator) {

        return IntStream.range(0, K)
                .boxed()
                .map(i -> instantiator.get())
                .toArray(Particle[]::new);
    }

    protected AtomicInteger[][] createCrystal(final int N, final int M) {
        return IntStream.range(0, N)
                .mapToObj(x -> IntStream.range(0, M)
                        .mapToObj(y -> new AtomicInteger())
                        .toArray(AtomicInteger[]::new))
                .toArray(AtomicInteger[][]::new);
    }

    protected void startThreads(final Thread[] threads) {

        for (final var thread : threads) thread.start();
    }

    protected void joinThreads(final Thread[] threads) {

        try {

            for (final var thread : threads) thread.join();

        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    }

    protected void interruptThreads(final Thread[] threads) {

        for (final var thread : threads) thread.interrupt();
    }

    protected void sleep(final int milliseconds) {

        try {

            Thread.sleep(milliseconds);

        } catch (final InterruptedException e) {

            e.printStackTrace();
        }
    }
}
