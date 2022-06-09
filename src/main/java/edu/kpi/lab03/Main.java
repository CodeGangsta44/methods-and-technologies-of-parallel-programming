package edu.kpi.lab03;

import edu.kpi.lab03.strategy.simulation.impl.LimitedBrownianMotionSimulationStrategy;
import edu.kpi.lab03.strategy.simulation.impl.TimedBrownianMotionSimulationStrategy;
import edu.kpi.lab03.utils.InputUtils;

public class Main {

    public static void main(final String... args) {

        final var timedBrownianMotionSimulationStrategy = new TimedBrownianMotionSimulationStrategy();
        final var limitedBrownianMotionSimulationStrategy = new LimitedBrownianMotionSimulationStrategy();

        final var baseParameter = InputUtils.readCommonAttributes();

        final var choice = InputUtils.readChoice();

        if (choice == 1) timedBrownianMotionSimulationStrategy.simulate(InputUtils.createTimedParameter(baseParameter));
        else if (choice == 2) limitedBrownianMotionSimulationStrategy.simulate(InputUtils.createLimitedParameter(baseParameter));
    }
}
