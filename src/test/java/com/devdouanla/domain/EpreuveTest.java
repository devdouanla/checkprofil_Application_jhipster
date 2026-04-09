package com.devdouanla.domain;

import static com.devdouanla.domain.CompetenceTestSamples.*;
import static com.devdouanla.domain.EpreuveTestSamples.*;
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
    void sessionsTest() {
        Epreuve epreuve = getEpreuveRandomSampleGenerator();
        SessionTest sessionTestBack = getSessionTestRandomSampleGenerator();

        epreuve.addSessions(sessionTestBack);
        assertThat(epreuve.getSessionses()).containsOnly(sessionTestBack);
        assertThat(sessionTestBack.getEpreuve()).isEqualTo(epreuve);

        epreuve.removeSessions(sessionTestBack);
        assertThat(epreuve.getSessionses()).doesNotContain(sessionTestBack);
        assertThat(sessionTestBack.getEpreuve()).isNull();

        epreuve.sessionses(new HashSet<>(Set.of(sessionTestBack)));
        assertThat(epreuve.getSessionses()).containsOnly(sessionTestBack);
        assertThat(sessionTestBack.getEpreuve()).isEqualTo(epreuve);

        epreuve.setSessionses(new HashSet<>());
        assertThat(epreuve.getSessionses()).doesNotContain(sessionTestBack);
        assertThat(sessionTestBack.getEpreuve()).isNull();
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
