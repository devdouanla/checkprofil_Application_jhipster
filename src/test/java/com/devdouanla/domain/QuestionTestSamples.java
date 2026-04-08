package com.devdouanla.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question().id(1L).enonce("enonce1").reponseTexte("reponseTexte1").points(1).explication("explication1");
    }

    public static Question getQuestionSample2() {
        return new Question().id(2L).enonce("enonce2").reponseTexte("reponseTexte2").points(2).explication("explication2");
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question()
            .id(longCount.incrementAndGet())
            .enonce(UUID.randomUUID().toString())
            .reponseTexte(UUID.randomUUID().toString())
            .points(intCount.incrementAndGet())
            .explication(UUID.randomUUID().toString());
    }
}
