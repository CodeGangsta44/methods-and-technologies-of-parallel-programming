package edu.kpi.lab03.parameter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LimitedBrownianMotionSimulationParameter extends BrownianMotionSimulationParameter {

    private int iterations;
    private int interval;
}
