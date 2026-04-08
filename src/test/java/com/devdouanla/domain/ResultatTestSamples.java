package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ResultatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Resultat getResultatSample1() {
        return new Resultat().id(1L);
    }

    public static Resultat getResultatSample2() {
        return new Resultat().id(2L);
    }

    public static Resultat getResultatRandomSampleGenerator() {
        return new Resultat().id(longCount.incrementAndGet());
    }
}
