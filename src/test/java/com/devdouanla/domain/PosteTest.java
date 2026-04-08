package com.devdouanla.domain;

import static com.devdouanla.domain.CompetenceRequiseTestSamples.*;
import static com.devdouanla.domain.EmployeTestSamples.*;
import static com.devdouanla.domain.ManagerTestSamples.*;
import static com.devdouanla.domain.PosteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PosteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poste.class);
        Poste poste1 = getPosteSample1();
        Poste poste2 = new Poste();
        assertThat(poste1).isNotEqualTo(poste2);

        poste2.setId(poste1.getId());
        assertThat(poste1).isEqualTo(poste2);

        poste2 = getPosteSample2();
        assertThat(poste1).isNotEqualTo(poste2);
    }

    @Test
    void employesTest() {
        Poste poste = getPosteRandomSampleGenerator();
        Employe employeBack = getEmployeRandomSampleGenerator();

        poste.addEmployes(employeBack);
        assertThat(poste.getEmployeses()).containsOnly(employeBack);
        assertThat(employeBack.getPoste()).isEqualTo(poste);

        poste.removeEmployes(employeBack);
        assertThat(poste.getEmployeses()).doesNotContain(employeBack);
        assertThat(employeBack.getPoste()).isNull();

        poste.employeses(new HashSet<>(Set.of(employeBack)));
        assertThat(poste.getEmployeses()).containsOnly(employeBack);
        assertThat(employeBack.getPoste()).isEqualTo(poste);

        poste.setEmployeses(new HashSet<>());
        assertThat(poste.getEmployeses()).doesNotContain(employeBack);
        assertThat(employeBack.getPoste()).isNull();
    }

    @Test
    void competencesRequisesTest() {
        Poste poste = getPosteRandomSampleGenerator();
        CompetenceRequise competenceRequiseBack = getCompetenceRequiseRandomSampleGenerator();

        poste.addCompetencesRequises(competenceRequiseBack);
        assertThat(poste.getCompetencesRequiseses()).containsOnly(competenceRequiseBack);
        assertThat(competenceRequiseBack.getPoste()).isEqualTo(poste);

        poste.removeCompetencesRequises(competenceRequiseBack);
        assertThat(poste.getCompetencesRequiseses()).doesNotContain(competenceRequiseBack);
        assertThat(competenceRequiseBack.getPoste()).isNull();

        poste.competencesRequiseses(new HashSet<>(Set.of(competenceRequiseBack)));
        assertThat(poste.getCompetencesRequiseses()).containsOnly(competenceRequiseBack);
        assertThat(competenceRequiseBack.getPoste()).isEqualTo(poste);

        poste.setCompetencesRequiseses(new HashSet<>());
        assertThat(poste.getCompetencesRequiseses()).doesNotContain(competenceRequiseBack);
        assertThat(competenceRequiseBack.getPoste()).isNull();
    }

    @Test
    void managerTest() {
        Poste poste = getPosteRandomSampleGenerator();
        Manager managerBack = getManagerRandomSampleGenerator();

        poste.setManager(managerBack);
        assertThat(poste.getManager()).isEqualTo(managerBack);

        poste.manager(null);
        assertThat(poste.getManager()).isNull();
    }
}
