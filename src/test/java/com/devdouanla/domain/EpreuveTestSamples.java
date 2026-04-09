package com.devdouanla.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EpreuveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Epreuve getEpreuveSample1() {
        return new Epreuve().id(1L).titre("titre1").enonce("enonce1").duree(1).nbQuestions(1);
    }

    public static Epreuve getEpreuveSample2() {
        return new Epreuve().id(2L).titre("titre2").enonce("enonce2").duree(2).nbQuestions(2);
    }

    public static Epreuve getEpreuveRandomSampleGenerator() {
        return new Epreuve()
            .id(longCount.incrementAndGet())
            .titre(UUID.randomUUID().toString())
            .enonce(UUID.randomUUID().toString())
            .duree(intCount.incrementAndGet())
            .nbQuestions(intCount.incrementAndGet());
    }
}
