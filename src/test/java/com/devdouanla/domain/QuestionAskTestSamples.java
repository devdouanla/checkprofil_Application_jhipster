package com.devdouanla.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuestionAskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuestionAsk getQuestionAskSample1() {
        return new QuestionAsk().id(1L).ordre(1);
    }

    public static QuestionAsk getQuestionAskSample2() {
        return new QuestionAsk().id(2L).ordre(2);
    }

    public static QuestionAsk getQuestionAskRandomSampleGenerator() {
        return new QuestionAsk().id(longCount.incrementAndGet()).ordre(intCount.incrementAndGet());
    }
}
