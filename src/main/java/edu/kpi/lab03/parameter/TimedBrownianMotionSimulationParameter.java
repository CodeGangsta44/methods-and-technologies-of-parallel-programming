package edu.kpi.lab03.parameter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TimedBrownianMotionSimulationParameter extends BrownianMotionSimulationParameter {

    private int totalTime;
    private int interval;
}
