package com.devdouanla.domain;

import static com.devdouanla.domain.EpreuveTestSamples.*;
import static com.devdouanla.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void epreuveTest() {
        Question question = getQuestionRandomSampleGenerator();
        Epreuve epreuveBack = getEpreuveRandomSampleGenerator();

        question.setEpreuve(epreuveBack);
        assertThat(question.getEpreuve()).isEqualTo(epreuveBack);

        question.epreuve(null);
        assertThat(question.getEpreuve()).isNull();
    }
}
