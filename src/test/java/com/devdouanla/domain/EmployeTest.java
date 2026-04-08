package com.devdouanla.domain;

import static com.devdouanla.domain.EmployeTestSamples.*;
import static com.devdouanla.domain.EvaluationTestSamples.*;
import static com.devdouanla.domain.PosteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employe.class);
        Employe employe1 = getEmployeSample1();
        Employe employe2 = new Employe();
        assertThat(employe1).isNotEqualTo(employe2);

        employe2.setId(employe1.getId());
        assertThat(employe1).isEqualTo(employe2);

        employe2 = getEmployeSample2();
        assertThat(employe1).isNotEqualTo(employe2);
    }

    @Test
    void evaluationsTest() {
        Employe employe = getEmployeRandomSampleGenerator();
        Evaluation evaluationBack = getEvaluationRandomSampleGenerator();

        employe.addEvaluations(evaluationBack);
        assertThat(employe.getEvaluationses()).containsOnly(evaluationBack);
        assertThat(evaluationBack.getEmploye()).isEqualTo(employe);

        employe.removeEvaluations(evaluationBack);
        assertThat(employe.getEvaluationses()).doesNotContain(evaluationBack);
        assertThat(evaluationBack.getEmploye()).isNull();

        employe.evaluationses(new HashSet<>(Set.of(evaluationBack)));
        assertThat(employe.getEvaluationses()).containsOnly(evaluationBack);
        assertThat(evaluationBack.getEmploye()).isEqualTo(employe);

        employe.setEvaluationses(new HashSet<>());
        assertThat(employe.getEvaluationses()).doesNotContain(evaluationBack);
        assertThat(evaluationBack.getEmploye()).isNull();
    }

    @Test
    void posteTest() {
        Employe employe = getEmployeRandomSampleGenerator();
        Poste posteBack = getPosteRandomSampleGenerator();

        employe.setPoste(posteBack);
        assertThat(employe.getPoste()).isEqualTo(posteBack);

        employe.poste(null);
        assertThat(employe.getPoste()).isNull();
    }
}
