package com.devdouanla.domain;

import static com.devdouanla.domain.QuestionAskTestSamples.*;
import static com.devdouanla.domain.ReponseCandidatTestSamples.*;
import static com.devdouanla.domain.SessionTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReponseCandidatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReponseCandidat.class);
        ReponseCandidat reponseCandidat1 = getReponseCandidatSample1();
        ReponseCandidat reponseCandidat2 = new ReponseCandidat();
        assertThat(reponseCandidat1).isNotEqualTo(reponseCandidat2);

        reponseCandidat2.setId(reponseCandidat1.getId());
        assertThat(reponseCandidat1).isEqualTo(reponseCandidat2);

        reponseCandidat2 = getReponseCandidatSample2();
        assertThat(reponseCandidat1).isNotEqualTo(reponseCandidat2);
    }

    @Test
    void questionAskTest() {
        ReponseCandidat reponseCandidat = getReponseCandidatRandomSampleGenerator();
        QuestionAsk questionAskBack = getQuestionAskRandomSampleGenerator();

        reponseCandidat.setQuestionAsk(questionAskBack);
        assertThat(reponseCandidat.getQuestionAsk()).isEqualTo(questionAskBack);

        reponseCandidat.questionAsk(null);
        assertThat(reponseCandidat.getQuestionAsk()).isNull();
    }

    @Test
    void sessionTest() {
        ReponseCandidat reponseCandidat = getReponseCandidatRandomSampleGenerator();
        SessionTest sessionTestBack = getSessionTestRandomSampleGenerator();

        reponseCandidat.setSession(sessionTestBack);
        assertThat(reponseCandidat.getSession()).isEqualTo(sessionTestBack);

        reponseCandidat.session(null);
        assertThat(reponseCandidat.getSession()).isNull();
    }
}
