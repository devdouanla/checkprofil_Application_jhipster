package com.devdouanla.domain;

import static com.devdouanla.domain.CompetenceTestSamples.*;
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
    void competenceTest() {
        Question question = getQuestionRandomSampleGenerator();
        Competence competenceBack = getCompetenceRandomSampleGenerator();

        question.setCompetence(competenceBack);
        assertThat(question.getCompetence()).isEqualTo(competenceBack);

        question.competence(null);
        assertThat(question.getCompetence()).isNull();
    }
}
