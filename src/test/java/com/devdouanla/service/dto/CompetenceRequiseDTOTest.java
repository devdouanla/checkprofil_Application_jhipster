package com.devdouanla.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompetenceRequiseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetenceRequiseDTO.class);
        CompetenceRequiseDTO competenceRequiseDTO1 = new CompetenceRequiseDTO();
        competenceRequiseDTO1.setId(1L);
        CompetenceRequiseDTO competenceRequiseDTO2 = new CompetenceRequiseDTO();
        assertThat(competenceRequiseDTO1).isNotEqualTo(competenceRequiseDTO2);
        competenceRequiseDTO2.setId(competenceRequiseDTO1.getId());
        assertThat(competenceRequiseDTO1).isEqualTo(competenceRequiseDTO2);
        competenceRequiseDTO2.setId(2L);
        assertThat(competenceRequiseDTO1).isNotEqualTo(competenceRequiseDTO2);
        competenceRequiseDTO1.setId(null);
        assertThat(competenceRequiseDTO1).isNotEqualTo(competenceRequiseDTO2);
    }
}
