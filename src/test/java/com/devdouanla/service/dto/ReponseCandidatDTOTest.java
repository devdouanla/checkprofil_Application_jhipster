package com.devdouanla.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReponseCandidatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReponseCandidatDTO.class);
        ReponseCandidatDTO reponseCandidatDTO1 = new ReponseCandidatDTO();
        reponseCandidatDTO1.setId(1L);
        ReponseCandidatDTO reponseCandidatDTO2 = new ReponseCandidatDTO();
        assertThat(reponseCandidatDTO1).isNotEqualTo(reponseCandidatDTO2);
        reponseCandidatDTO2.setId(reponseCandidatDTO1.getId());
        assertThat(reponseCandidatDTO1).isEqualTo(reponseCandidatDTO2);
        reponseCandidatDTO2.setId(2L);
        assertThat(reponseCandidatDTO1).isNotEqualTo(reponseCandidatDTO2);
        reponseCandidatDTO1.setId(null);
        assertThat(reponseCandidatDTO1).isNotEqualTo(reponseCandidatDTO2);
    }
}
