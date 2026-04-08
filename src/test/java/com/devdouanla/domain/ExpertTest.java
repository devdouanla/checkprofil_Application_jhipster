package com.devdouanla.domain;

import static com.devdouanla.domain.CompetenceTestSamples.*;
import static com.devdouanla.domain.ExpertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExpertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expert.class);
        Expert expert1 = getExpertSample1();
        Expert expert2 = new Expert();
        assertThat(expert1).isNotEqualTo(expert2);

        expert2.setId(expert1.getId());
        assertThat(expert1).isEqualTo(expert2);

        expert2 = getExpertSample2();
        assertThat(expert1).isNotEqualTo(expert2);
    }

    @Test
    void competencesTest() {
        Expert expert = getExpertRandomSampleGenerator();
        Competence competenceBack = getCompetenceRandomSampleGenerator();

        expert.addCompetences(competenceBack);
        assertThat(expert.getCompetenceses()).containsOnly(competenceBack);
        assertThat(competenceBack.getExpertses()).containsOnly(expert);

        expert.removeCompetences(competenceBack);
        assertThat(expert.getCompetenceses()).doesNotContain(competenceBack);
        assertThat(competenceBack.getExpertses()).doesNotContain(expert);

        expert.competenceses(new HashSet<>(Set.of(competenceBack)));
        assertThat(expert.getCompetenceses()).containsOnly(competenceBack);
        assertThat(competenceBack.getExpertses()).containsOnly(expert);

        expert.setCompetenceses(new HashSet<>());
        assertThat(expert.getCompetenceses()).doesNotContain(competenceBack);
        assertThat(competenceBack.getExpertses()).doesNotContain(expert);
    }
}
