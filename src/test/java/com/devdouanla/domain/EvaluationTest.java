package com.devdouanla.domain;

import static com.devdouanla.domain.EmployeTestSamples.*;
import static com.devdouanla.domain.EvaluationTestSamples.*;
import static com.devdouanla.domain.SessionTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EvaluationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluation.class);
        Evaluation evaluation1 = getEvaluationSample1();
        Evaluation evaluation2 = new Evaluation();
        assertThat(evaluation1).isNotEqualTo(evaluation2);

        evaluation2.setId(evaluation1.getId());
        assertThat(evaluation1).isEqualTo(evaluation2);

        evaluation2 = getEvaluationSample2();
        assertThat(evaluation1).isNotEqualTo(evaluation2);
    }

    @Test
    void sessionsTest() {
        Evaluation evaluation = getEvaluationRandomSampleGenerator();
        SessionTest sessionTestBack = getSessionTestRandomSampleGenerator();

        evaluation.addSessions(sessionTestBack);
        assertThat(evaluation.getSessionses()).containsOnly(sessionTestBack);
        assertThat(sessionTestBack.getEvaluation()).isEqualTo(evaluation);

        evaluation.removeSessions(sessionTestBack);
        assertThat(evaluation.getSessionses()).doesNotContain(sessionTestBack);
        assertThat(sessionTestBack.getEvaluation()).isNull();

        evaluation.sessionses(new HashSet<>(Set.of(sessionTestBack)));
        assertThat(evaluation.getSessionses()).containsOnly(sessionTestBack);
        assertThat(sessionTestBack.getEvaluation()).isEqualTo(evaluation);

        evaluation.setSessionses(new HashSet<>());
        assertThat(evaluation.getSessionses()).doesNotContain(sessionTestBack);
        assertThat(sessionTestBack.getEvaluation()).isNull();
    }

    @Test
    void employeTest() {
        Evaluation evaluation = getEvaluationRandomSampleGenerator();
        Employe employeBack = getEmployeRandomSampleGenerator();

        evaluation.setEmploye(employeBack);
        assertThat(evaluation.getEmploye()).isEqualTo(employeBack);

        evaluation.employe(null);
        assertThat(evaluation.getEmploye()).isNull();
    }
}
