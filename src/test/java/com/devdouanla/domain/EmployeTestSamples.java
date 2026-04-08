package com.devdouanla.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Employe getEmployeSample1() {
        return new Employe().id(1L).nom("nom1");
    }

    public static Employe getEmployeSample2() {
        return new Employe().id(2L).nom("nom2");
    }

    public static Employe getEmployeRandomSampleGenerator() {
        return new Employe().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
