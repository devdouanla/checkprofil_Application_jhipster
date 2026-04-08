package com.devdouanla.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultatDTO.class);
        ResultatDTO resultatDTO1 = new ResultatDTO();
        resultatDTO1.setId(1L);
        ResultatDTO resultatDTO2 = new ResultatDTO();
        assertThat(resultatDTO1).isNotEqualTo(resultatDTO2);
        resultatDTO2.setId(resultatDTO1.getId());
        assertThat(resultatDTO1).isEqualTo(resultatDTO2);
        resultatDTO2.setId(2L);
        assertThat(resultatDTO1).isNotEqualTo(resultatDTO2);
        resultatDTO1.setId(null);
        assertThat(resultatDTO1).isNotEqualTo(resultatDTO2);
    }
}
