package com.devdouanla.domain;

import static com.devdouanla.domain.EpreuveTestSamples.*;
import static com.devdouanla.domain.EvaluationTestSamples.*;
import static com.devdouanla.domain.QuestionAskTestSamples.*;
import static com.devdouanla.domain.ReponseCandidatTestSamples.*;
import static com.devdouanla.domain.ResultatTestSamples.*;
import static com.devdouanla.domain.SessionTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SessionTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionTest.class);
        SessionTest sessionTest1 = getSessionTestSample1();
        SessionTest sessionTest2 = new SessionTest();
        assertThat(sessionTest1).isNotEqualTo(sessionTest2);

        sessionTest2.setId(sessionTest1.getId());
        assertThat(sessionTest1).isEqualTo(sessionTest2);

        sessionTest2 = getSessionTestSample2();
        assertThat(sessionTest1).isNotEqualTo(sessionTest2);
    }

    @Test
    void resultatTest() {
        SessionTest sessionTest = getSessionTestRandomSampleGenerator();
        Resultat resultatBack = getResultatRandomSampleGenerator();

        sessionTest.setResultat(resultatBack);
        assertThat(sessionTest.getResultat()).isEqualTo(resultatBack);

        sessionTest.resultat(null);
        assertThat(sessionTest.getResultat()).isNull();
    }

    @Test
    void questionsAskTest() {
        SessionTest sessionTest = getSessionTestRandomSampleGenerator();
        QuestionAsk questionAskBack = getQuestionAskRandomSampleGenerator();

        sessionTest.addQuestionsAsk(questionAskBack);
        assertThat(sessionTest.getQuestionsAsks()).containsOnly(questionAskBack);
        assertThat(questionAskBack.getSession()).isEqualTo(sessionTest);

        sessionTest.removeQuestionsAsk(questionAskBack);
        assertThat(sessionTest.getQuestionsAsks()).doesNotContain(questionAskBack);
        assertThat(questionAskBack.getSession()).isNull();

        sessionTest.questionsAsks(new HashSet<>(Set.of(questionAskBack)));
        assertThat(sessionTest.getQuestionsAsks()).containsOnly(questionAskBack);
        assertThat(questionAskBack.getSession()).isEqualTo(sessionTest);

        sessionTest.setQuestionsAsks(new HashSet<>());
        assertThat(sessionTest.getQuestionsAsks()).doesNotContain(questionAskBack);
        assertThat(questionAskBack.getSession()).isNull();
    }

    @Test
    void reponsesTest() {
        SessionTest sessionTest = getSessionTestRandomSampleGenerator();
        ReponseCandidat reponseCandidatBack = getReponseCandidatRandomSampleGenerator();

        sessionTest.addReponses(reponseCandidatBack);
        assertThat(sessionTest.getReponseses()).containsOnly(reponseCandidatBack);
        assertThat(reponseCandidatBack.getSession()).isEqualTo(sessionTest);

        sessionTest.removeReponses(reponseCandidatBack);
        assertThat(sessionTest.getReponseses()).doesNotContain(reponseCandidatBack);
        assertThat(reponseCandidatBack.getSession()).isNull();

        sessionTest.reponseses(new HashSet<>(Set.of(reponseCandidatBack)));
        assertThat(sessionTest.getReponseses()).containsOnly(reponseCandidatBack);
        assertThat(reponseCandidatBack.getSession()).isEqualTo(sessionTest);

        sessionTest.setReponseses(new HashSet<>());
        assertThat(sessionTest.getReponseses()).doesNotContain(reponseCandidatBack);
        assertThat(reponseCandidatBack.getSession()).isNull();
    }

    @Test
    void evaluationTest() {
        SessionTest sessionTest = getSessionTestRandomSampleGenerator();
        Evaluation evaluationBack = getEvaluationRandomSampleGenerator();

        sessionTest.setEvaluation(evaluationBack);
        assertThat(sessionTest.getEvaluation()).isEqualTo(evaluationBack);

        sessionTest.evaluation(null);
        assertThat(sessionTest.getEvaluation()).isNull();
    }

    @Test
    void epreuveTest() {
        SessionTest sessionTest = getSessionTestRandomSampleGenerator();
        Epreuve epreuveBack = getEpreuveRandomSampleGenerator();

        sessionTest.setEpreuve(epreuveBack);
        assertThat(sessionTest.getEpreuve()).isEqualTo(epreuveBack);

        sessionTest.epreuve(null);
        assertThat(sessionTest.getEpreuve()).isNull();
    }
}
