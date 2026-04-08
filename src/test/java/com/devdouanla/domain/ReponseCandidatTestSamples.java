package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReponseCandidatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ReponseCandidat getReponseCandidatSample1() {
        return new ReponseCandidat().id(1L);
    }

    public static ReponseCandidat getReponseCandidatSample2() {
        return new ReponseCandidat().id(2L);
    }

    public static ReponseCandidat getReponseCandidatRandomSampleGenerator() {
        return new ReponseCandidat().id(longCount.incrementAndGet());
    }
}
