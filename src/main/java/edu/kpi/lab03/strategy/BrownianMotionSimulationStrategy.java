package edu.kpi.lab03.strategy;

import edu.kpi.lab03.parameter.BrownianMotionSimulationParameter;

public interface BrownianMotionSimulationStrategy<T extends BrownianMotionSimulationParameter> {

    void simulate(final T parameter);
}
