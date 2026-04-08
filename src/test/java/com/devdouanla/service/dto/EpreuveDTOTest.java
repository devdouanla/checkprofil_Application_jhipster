package com.devdouanla.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EpreuveDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EpreuveDTO.class);
        EpreuveDTO epreuveDTO1 = new EpreuveDTO();
        epreuveDTO1.setId(1L);
        EpreuveDTO epreuveDTO2 = new EpreuveDTO();
        assertThat(epreuveDTO1).isNotEqualTo(epreuveDTO2);
        epreuveDTO2.setId(epreuveDTO1.getId());
        assertThat(epreuveDTO1).isEqualTo(epreuveDTO2);
        epreuveDTO2.setId(2L);
        assertThat(epreuveDTO1).isNotEqualTo(epreuveDTO2);
        epreuveDTO1.setId(null);
        assertThat(epreuveDTO1).isNotEqualTo(epreuveDTO2);
    }
}
