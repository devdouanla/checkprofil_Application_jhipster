package com.devdouanla.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RHDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RHDTO.class);
        RHDTO rHDTO1 = new RHDTO();
        rHDTO1.setId(1L);
        RHDTO rHDTO2 = new RHDTO();
        assertThat(rHDTO1).isNotEqualTo(rHDTO2);
        rHDTO2.setId(rHDTO1.getId());
        assertThat(rHDTO1).isEqualTo(rHDTO2);
        rHDTO2.setId(2L);
        assertThat(rHDTO1).isNotEqualTo(rHDTO2);
        rHDTO1.setId(null);
        assertThat(rHDTO1).isNotEqualTo(rHDTO2);
    }
}
