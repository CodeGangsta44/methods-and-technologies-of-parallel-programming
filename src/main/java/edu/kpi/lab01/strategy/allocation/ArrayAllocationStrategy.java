package edu.kpi.lab01.strategy.allocation;

public class ArrayAllocationStrategy {

    private final int bytesToAllocate;

    public ArrayAllocationStrategy(final int bytesToAllocate) {

        this.bytesToAllocate = bytesToAllocate;
    }

    public void execute()
    {
        final var array = new int[bytesToAllocate / Integer.BYTES];
    }
}
