package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ExpertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Expert getExpertSample1() {
        return new Expert().id(1L);
    }

    public static Expert getExpertSample2() {
        return new Expert().id(2L);
    }

    public static Expert getExpertRandomSampleGenerator() {
        return new Expert().id(longCount.incrementAndGet());
    }
}
