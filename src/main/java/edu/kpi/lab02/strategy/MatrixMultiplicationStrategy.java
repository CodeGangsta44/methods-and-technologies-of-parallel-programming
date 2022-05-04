package edu.kpi.lab02.strategy;

public interface MatrixMultiplicationStrategy<T extends Number> {

    void multiply(final T[][] firstMatrix, final T[][] secondMatrix, final T[][] result);
}
