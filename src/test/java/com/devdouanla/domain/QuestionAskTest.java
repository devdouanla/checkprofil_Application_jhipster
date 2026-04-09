package com.devdouanla.domain;

import static com.devdouanla.domain.QuestionAskTestSamples.*;
import static com.devdouanla.domain.QuestionTestSamples.*;
import static com.devdouanla.domain.SessionTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionAskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionAsk.class);
        QuestionAsk questionAsk1 = getQuestionAskSample1();
        QuestionAsk questionAsk2 = new QuestionAsk();
        assertThat(questionAsk1).isNotEqualTo(questionAsk2);

        questionAsk2.setId(questionAsk1.getId());
        assertThat(questionAsk1).isEqualTo(questionAsk2);

        questionAsk2 = getQuestionAskSample2();
        assertThat(questionAsk1).isNotEqualTo(questionAsk2);
    }

    @Test
    void questionTest() {
        QuestionAsk questionAsk = getQuestionAskRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        questionAsk.setQuestion(questionBack);
        assertThat(questionAsk.getQuestion()).isEqualTo(questionBack);

        questionAsk.question(null);
        assertThat(questionAsk.getQuestion()).isNull();
    }

    @Test
    void sessionTest() {
        QuestionAsk questionAsk = getQuestionAskRandomSampleGenerator();
        SessionTest sessionTestBack = getSessionTestRandomSampleGenerator();

        questionAsk.setSession(sessionTestBack);
        assertThat(questionAsk.getSession()).isEqualTo(sessionTestBack);

        questionAsk.session(null);
        assertThat(questionAsk.getSession()).isNull();
    }
}
