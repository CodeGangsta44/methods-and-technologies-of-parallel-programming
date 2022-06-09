package edu.kpi.lab03.strategy.impl;

import edu.kpi.lab03.parameter.TimedBrownianMotionSimulationParameter;
import edu.kpi.lab03.particle.Particle;
import edu.kpi.lab03.strategy.AbstractSimulationStrategy;
import edu.kpi.lab03.strategy.BrownianMotionSimulationStrategy;
import edu.kpi.lab03.utils.DisplayUtils;

import java.util.concurrent.Phaser;

public class TimedBrownianMotionSimulationStrategy extends AbstractSimulationStrategy implements BrownianMotionSimulationStrategy<TimedBrownianMotionSimulationParameter> {

    @Override
    public void simulate(final TimedBrownianMotionSimulationParameter parameter) {

        final var crystal = createCrystal(parameter.getN(), parameter.getM());
        crystal[0][0].addAndGet(parameter.getK());

        final var phaser = new Phaser(1);
        final var particles = createThreads(parameter.getK(),
                () -> new Particle(crystal, parameter.getXProbability(), parameter.getYProbability(), phaser));

        startThreads(particles);

        for (var i = 0; i < parameter.getTotalTime() / parameter.getInterval(); i++) {

            phaser.arriveAndAwaitAdvance();
            DisplayUtils.displayCrystal(crystal, parameter.getK(), i);
            phaser.arriveAndAwaitAdvance();

            sleep(parameter.getInterval());
        }

        interruptThreads(particles);
        phaser.arriveAndDeregister();
    }
}
