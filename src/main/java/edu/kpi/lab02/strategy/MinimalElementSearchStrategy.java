package edu.kpi.lab02.strategy;

import edu.kpi.lab02.data.Pair;

public interface MinimalElementSearchStrategy<T extends Number> {

    Pair find(final T[][] matrix);
}
