package com.devdouanla.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SessionTestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionTestDTO.class);
        SessionTestDTO sessionTestDTO1 = new SessionTestDTO();
        sessionTestDTO1.setId(1L);
        SessionTestDTO sessionTestDTO2 = new SessionTestDTO();
        assertThat(sessionTestDTO1).isNotEqualTo(sessionTestDTO2);
        sessionTestDTO2.setId(sessionTestDTO1.getId());
        assertThat(sessionTestDTO1).isEqualTo(sessionTestDTO2);
        sessionTestDTO2.setId(2L);
        assertThat(sessionTestDTO1).isNotEqualTo(sessionTestDTO2);
        sessionTestDTO1.setId(null);
        assertThat(sessionTestDTO1).isNotEqualTo(sessionTestDTO2);
    }
}
