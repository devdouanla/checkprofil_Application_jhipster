package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SessionTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static SessionTest getSessionTestSample1() {
        return new SessionTest().id(1L);
    }

    public static SessionTest getSessionTestSample2() {
        return new SessionTest().id(2L);
    }

    public static SessionTest getSessionTestRandomSampleGenerator() {
        return new SessionTest().id(longCount.incrementAndGet());
    }
}
