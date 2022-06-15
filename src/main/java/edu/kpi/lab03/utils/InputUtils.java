package edu.kpi.lab03.utils;

import edu.kpi.lab03.controller.InputController;
import edu.kpi.lab03.parameter.BrownianMotionSimulationParameter;
import edu.kpi.lab03.parameter.LimitedBrownianMotionSimulationParameter;
import edu.kpi.lab03.parameter.TimedBrownianMotionSimulationParameter;

public class InputUtils {

    public static BrownianMotionSimulationParameter readCommonAttributes() {

        final var N = InputController.readInt("Please, enter first dimension of matrix. N = ", 0, Integer.MAX_VALUE);
        final var M = InputController.readInt("Please, enter second dimension of matrix. M = ", 0, Integer.MAX_VALUE);
        final var K = InputController.readInt("Please, enter quantity of particles. K = ", 0, Integer.MAX_VALUE);
        final var xProbability = InputController.readDouble("Please, enter probability for X axis in range [0; 1]. xProbability = ", 0.0, 1.0);
        final var yProbability = InputController.readDouble("Please, enter probability for Y axis in range [0; 1]. yProbability = ", 0.0, 1.0);

        final var parameter = new BrownianMotionSimulationParameter();

        parameter.setN(N);
        parameter.setM(M);
        parameter.setK(K);
        parameter.setXProbability(xProbability);
        parameter.setYProbability(yProbability);

        return parameter;
    }

    public static int readChoice() {

        return InputController.readInt("Please, select simulation method:\n" +
                " 1 - Timed realtime simulation\n" +
                " 2 - Limited by iterations simulation\n" +
                "CHOICE = ", 1, 2);
    }

    public static TimedBrownianMotionSimulationParameter createTimedParameter(final BrownianMotionSimulationParameter baseParameter) {

        final var totalTime = InputController.readInt("Please, enter total time of execution in milliseconds. TOTAL_TIME = ", 0, Integer.MAX_VALUE);
        final var interval = InputController.readInt("Please, enter interval of execution in milliseconds. INTERVAL = ", 0, Integer.MAX_VALUE);

        final var parameter = TimedBrownianMotionSimulationParameter.builder()
                .totalTime(totalTime)
                .interval(interval)
                .build();

        cloneCommonAttributes(baseParameter, parameter);

        return parameter;
    }

    public static LimitedBrownianMotionSimulationParameter createLimitedParameter(final BrownianMotionSimulationParameter baseParameter) {

        final var iterations = InputController.readInt("Please, enter total number of iterations. ITERATIONS = ", 1, Integer.MAX_VALUE);
        final var interval = InputController.readInt("Please, enter interval for intermediate state display in milliseconds. INTERVAL = ", 0, Integer.MAX_VALUE);

        final var parameter = LimitedBrownianMotionSimulationParameter.builder()
                .iterations(iterations)
                .interval(interval)
                .build();

        cloneCommonAttributes(baseParameter, parameter);

        return parameter;
    }

    private static void cloneCommonAttributes(final BrownianMotionSimulationParameter source,
                                              final BrownianMotionSimulationParameter target) {

        target.setN(source.getN());
        target.setM(source.getM());
        target.setK(source.getK());
        target.setXProbability(source.getXProbability());
        target.setYProbability(source.getYProbability());
    }
}
