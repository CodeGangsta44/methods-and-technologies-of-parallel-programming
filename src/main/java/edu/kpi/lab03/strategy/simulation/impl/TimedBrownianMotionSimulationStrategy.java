package edu.kpi.lab03.strategy.simulation.impl;

import edu.kpi.lab03.parameter.TimedBrownianMotionSimulationParameter;
import edu.kpi.lab03.particle.Particle;
import edu.kpi.lab03.strategy.simulation.AbstractSimulationStrategy;
import edu.kpi.lab03.strategy.simulation.BrownianMotionSimulationStrategy;
import edu.kpi.lab03.utils.DisplayUtils;

import java.util.concurrent.Phaser;

public class TimedBrownianMotionSimulationStrategy extends AbstractSimulationStrategy implements BrownianMotionSimulationStrategy<TimedBrownianMotionSimulationParameter> {

    @Override
    public void simulate(final TimedBrownianMotionSimulationParameter parameter) {

        final var crystal = createCrystal(parameter.getN(), parameter.getM());
        crystal[0][0].addAndGet(parameter.getK());

        final var phaser = new Phaser(1);
        final var particles = createThreads(parameter.getK(),
                () -> new Particle(crystal, parameter.getXProbability(), parameter.getYProbability(), phaser, Boolean.TRUE));

        startThreads(particles);

        final var startTime = System.currentTimeMillis();

        for (var i = 0; parameter.getTotalTime() > (System.currentTimeMillis() - startTime); i++) {

            phaser.arriveAndAwaitAdvance();
            DisplayUtils.displayCrystal(crystal, parameter.getK(), i);
            phaser.arriveAndAwaitAdvance();

            sleep(parameter.getInterval());
        }

        interruptThreads(particles);
        phaser.arriveAndDeregister();
    }
}
