package com.devdouanla.domain;

import static com.devdouanla.domain.CompetenceRequiseTestSamples.*;
import static com.devdouanla.domain.CompetenceTestSamples.*;
import static com.devdouanla.domain.PosteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompetenceRequiseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetenceRequise.class);
        CompetenceRequise competenceRequise1 = getCompetenceRequiseSample1();
        CompetenceRequise competenceRequise2 = new CompetenceRequise();
        assertThat(competenceRequise1).isNotEqualTo(competenceRequise2);

        competenceRequise2.setId(competenceRequise1.getId());
        assertThat(competenceRequise1).isEqualTo(competenceRequise2);

        competenceRequise2 = getCompetenceRequiseSample2();
        assertThat(competenceRequise1).isNotEqualTo(competenceRequise2);
    }

    @Test
    void competenceTest() {
        CompetenceRequise competenceRequise = getCompetenceRequiseRandomSampleGenerator();
        Competence competenceBack = getCompetenceRandomSampleGenerator();

        competenceRequise.setCompetence(competenceBack);
        assertThat(competenceRequise.getCompetence()).isEqualTo(competenceBack);

        competenceRequise.competence(null);
        assertThat(competenceRequise.getCompetence()).isNull();
    }

    @Test
    void posteTest() {
        CompetenceRequise competenceRequise = getCompetenceRequiseRandomSampleGenerator();
        Poste posteBack = getPosteRandomSampleGenerator();

        competenceRequise.setPoste(posteBack);
        assertThat(competenceRequise.getPoste()).isEqualTo(posteBack);

        competenceRequise.poste(null);
        assertThat(competenceRequise.getPoste()).isNull();
    }
}
