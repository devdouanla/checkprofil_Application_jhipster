package com.devdouanla.domain;

import static com.devdouanla.domain.CompetenceTestSamples.*;
import static com.devdouanla.domain.EpreuveTestSamples.*;
import static com.devdouanla.domain.ExpertTestSamples.*;
import static com.devdouanla.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompetenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Competence.class);
        Competence competence1 = getCompetenceSample1();
        Competence competence2 = new Competence();
        assertThat(competence1).isNotEqualTo(competence2);

        competence2.setId(competence1.getId());
        assertThat(competence1).isEqualTo(competence2);

        competence2 = getCompetenceSample2();
        assertThat(competence1).isNotEqualTo(competence2);
    }

    @Test
    void questionsTest() {
        Competence competence = getCompetenceRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        competence.addQuestions(questionBack);
        assertThat(competence.getQuestionses()).containsOnly(questionBack);
        assertThat(questionBack.getCompetence()).isEqualTo(competence);

        competence.removeQuestions(questionBack);
        assertThat(competence.getQuestionses()).doesNotContain(questionBack);
        assertThat(questionBack.getCompetence()).isNull();

        competence.questionses(new HashSet<>(Set.of(questionBack)));
        assertThat(competence.getQuestionses()).containsOnly(questionBack);
        assertThat(questionBack.getCompetence()).isEqualTo(competence);

        competence.setQuestionses(new HashSet<>());
        assertThat(competence.getQuestionses()).doesNotContain(questionBack);
        assertThat(questionBack.getCompetence()).isNull();
    }

    @Test
    void expertsTest() {
        Competence competence = getCompetenceRandomSampleGenerator();
        Expert expertBack = getExpertRandomSampleGenerator();

        competence.addExperts(expertBack);
        assertThat(competence.getExpertses()).containsOnly(expertBack);

        competence.removeExperts(expertBack);
        assertThat(competence.getExpertses()).doesNotContain(expertBack);

        competence.expertses(new HashSet<>(Set.of(expertBack)));
        assertThat(competence.getExpertses()).containsOnly(expertBack);

        competence.setExpertses(new HashSet<>());
        assertThat(competence.getExpertses()).doesNotContain(expertBack);
    }

    @Test
    void epreuvesTest() {
        Competence competence = getCompetenceRandomSampleGenerator();
        Epreuve epreuveBack = getEpreuveRandomSampleGenerator();

        competence.addEpreuves(epreuveBack);
        assertThat(competence.getEpreuveses()).containsOnly(epreuveBack);
        assertThat(epreuveBack.getCompetence()).isEqualTo(competence);

        competence.removeEpreuves(epreuveBack);
        assertThat(competence.getEpreuveses()).doesNotContain(epreuveBack);
        assertThat(epreuveBack.getCompetence()).isNull();

        competence.epreuveses(new HashSet<>(Set.of(epreuveBack)));
        assertThat(competence.getEpreuveses()).containsOnly(epreuveBack);
        assertThat(epreuveBack.getCompetence()).isEqualTo(competence);

        competence.setEpreuveses(new HashSet<>());
        assertThat(competence.getEpreuveses()).doesNotContain(epreuveBack);
        assertThat(epreuveBack.getCompetence()).isNull();
    }
}
