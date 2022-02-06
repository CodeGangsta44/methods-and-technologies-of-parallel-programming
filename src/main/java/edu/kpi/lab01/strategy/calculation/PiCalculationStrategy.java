package edu.kpi.lab01.strategy.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PiCalculationStrategy {

    private static final String REACHED_ITERATIONS_LIMIT_MESSAGE = "Iterations limit was reached";

    private static final BigDecimal BASE = BigDecimal.valueOf(4);
    private static final int ITERATIONS_LIMIT = 10000000;
    private final int digitsAfterComa;

    public PiCalculationStrategy(final int digitsAfterComa) {

        this.digitsAfterComa = digitsAfterComa;
    }

    public String execute()
    {
        BigDecimal result = BigDecimal.ZERO;


        for (long i = 0; i < ITERATIONS_LIMIT; i++) {

            BigDecimal toAdd = BASE.divide(BigDecimal.valueOf((i * 2) + 1), 1000, RoundingMode.HALF_UP);

            if (i % 2 == 1) {
                toAdd = toAdd.negate();
            }

            result = result.add(toAdd);

            if (i % 1000 == 0 && ((toAdd.scale() - toAdd.unscaledValue().toString().length()) == digitsAfterComa + 1)) {

                return result.toString().substring(0, 2 + digitsAfterComa);
            }
        }

        throw new IllegalStateException(REACHED_ITERATIONS_LIMIT_MESSAGE);
    }
}
