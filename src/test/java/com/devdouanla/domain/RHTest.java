package com.devdouanla.domain;

import static com.devdouanla.domain.RHTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RHTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RH.class);
        RH rH1 = getRHSample1();
        RH rH2 = new RH();
        assertThat(rH1).isNotEqualTo(rH2);

        rH2.setId(rH1.getId());
        assertThat(rH1).isEqualTo(rH2);

        rH2 = getRHSample2();
        assertThat(rH1).isNotEqualTo(rH2);
    }
}
