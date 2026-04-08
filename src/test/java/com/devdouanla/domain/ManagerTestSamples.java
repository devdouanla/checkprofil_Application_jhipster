package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ManagerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Manager getManagerSample1() {
        return new Manager().id(1L);
    }

    public static Manager getManagerSample2() {
        return new Manager().id(2L);
    }

    public static Manager getManagerRandomSampleGenerator() {
        return new Manager().id(longCount.incrementAndGet());
    }
}
