package edu.kpi.lab03.strategy.impl;

import edu.kpi.lab03.parameter.LimitedBrownianMotionSimulationParameter;
import edu.kpi.lab03.particle.Particle;
import edu.kpi.lab03.strategy.AbstractSimulationStrategy;
import edu.kpi.lab03.strategy.BrownianMotionSimulationStrategy;
import edu.kpi.lab03.utils.DisplayUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class LimitedBrownianMotionSimulationStrategy extends AbstractSimulationStrategy implements BrownianMotionSimulationStrategy<LimitedBrownianMotionSimulationParameter> {

    @Override
    public void simulate(final LimitedBrownianMotionSimulationParameter parameter) {

        final AtomicInteger[][] crystal = createCrystal(parameter.getN(), parameter.getM());

        crystal[0][0].addAndGet(parameter.getK());

        final var particles = createThreads(parameter.getK(),
                () -> new Particle(crystal, parameter.getXProbability(), parameter.getYProbability(), parameter.getIterations()));

        startThreads(particles);
        joinThreads(particles);

        DisplayUtils.displayCrystal(crystal, parameter.getK());
    }
}
