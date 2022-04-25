package edu.kpi.lab02.strategy;

public interface MatrixMultiplicationStrategy<T extends Number> {

    T[][] multiply(final T[][] firstMatrix, final T[][] secondMatrix);
}
