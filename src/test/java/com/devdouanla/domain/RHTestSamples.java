package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RHTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static RH getRHSample1() {
        return new RH().id(1L);
    }

    public static RH getRHSample2() {
        return new RH().id(2L);
    }

    public static RH getRHRandomSampleGenerator() {
        return new RH().id(longCount.incrementAndGet());
    }
}
