package edu.kpi.lab02.generator.impl;

import edu.kpi.lab02.generator.Generator;

import java.util.Random;

public class RandomIntegerGenerator implements Generator<Integer> {

    private Random random = new Random();

    @Override
    public Integer generate() {

        return random.nextInt();
    }
}
