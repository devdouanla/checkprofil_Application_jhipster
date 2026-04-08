package com.devdouanla.domain;

import static com.devdouanla.domain.CompetenceTestSamples.*;
import static com.devdouanla.domain.EpreuveTestSamples.*;
import static com.devdouanla.domain.QuestionTestSamples.*;
import static com.devdouanla.domain.SessionTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EpreuveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Epreuve.class);
        Epreuve epreuve1 = getEpreuveSample1();
        Epreuve epreuve2 = new Epreuve();
        assertThat(epreuve1).isNotEqualTo(epreuve2);

        epreuve2.setId(epreuve1.getId());
        assertThat(epreuve1).isEqualTo(epreuve2);

        epreuve2 = getEpreuveSample2();
        assertThat(epreuve1).isNotEqualTo(epreuve2);
    }

    @Test
    void questionsTest() {
        Epreuve epreuve = getEpreuveRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        epreuve.addQuestions(questionBack);
        assertThat(epreuve.getQuestionses()).containsOnly(questionBack);
        assertThat(questionBack.getEpreuve()).isEqualTo(epreuve);

        epreuve.removeQuestions(questionBack);
        assertThat(epreuve.getQuestionses()).doesNotContain(questionBack);
        assertThat(questionBack.getEpreuve()).isNull();

        epreuve.questionses(new HashSet<>(Set.of(questionBack)));
        assertThat(epreuve.getQuestionses()).containsOnly(questionBack);
        assertThat(questionBack.getEpreuve()).isEqualTo(epreuve);

        epreuve.setQuestionses(new HashSet<>());
        assertThat(epreuve.getQuestionses()).doesNotContain(questionBack);
        assertThat(questionBack.getEpreuve()).isNull();
    }

    @Test
    void sessionTestTest() {
        Epreuve epreuve = getEpreuveRandomSampleGenerator();
        SessionTest sessionTestBack = getSessionTestRandomSampleGenerator();

        epreuve.addSessionTest(sessionTestBack);
        assertThat(epreuve.getSessionTests()).containsOnly(sessionTestBack);
        assertThat(sessionTestBack.getEpreuves()).isEqualTo(epreuve);

        epreuve.removeSessionTest(sessionTestBack);
        assertThat(epreuve.getSessionTests()).doesNotContain(sessionTestBack);
        assertThat(sessionTestBack.getEpreuves()).isNull();

        epreuve.sessionTests(new HashSet<>(Set.of(sessionTestBack)));
        assertThat(epreuve.getSessionTests()).containsOnly(sessionTestBack);
        assertThat(sessionTestBack.getEpreuves()).isEqualTo(epreuve);

        epreuve.setSessionTests(new HashSet<>());
        assertThat(epreuve.getSessionTests()).doesNotContain(sessionTestBack);
        assertThat(sessionTestBack.getEpreuves()).isNull();
    }

    @Test
    void competenceTest() {
        Epreuve epreuve = getEpreuveRandomSampleGenerator();
        Competence competenceBack = getCompetenceRandomSampleGenerator();

        epreuve.setCompetence(competenceBack);
        assertThat(epreuve.getCompetence()).isEqualTo(competenceBack);

        epreuve.competence(null);
        assertThat(epreuve.getCompetence()).isNull();
    }
}
