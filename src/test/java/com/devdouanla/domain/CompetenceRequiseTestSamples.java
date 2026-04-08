package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CompetenceRequiseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static CompetenceRequise getCompetenceRequiseSample1() {
        return new CompetenceRequise().id(1L);
    }

    public static CompetenceRequise getCompetenceRequiseSample2() {
        return new CompetenceRequise().id(2L);
    }

    public static CompetenceRequise getCompetenceRequiseRandomSampleGenerator() {
        return new CompetenceRequise().id(longCount.incrementAndGet());
    }
}
