package com.devdouanla.domain;

import static com.devdouanla.domain.ResultatTestSamples.*;
import static com.devdouanla.domain.SessionTestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resultat.class);
        Resultat resultat1 = getResultatSample1();
        Resultat resultat2 = new Resultat();
        assertThat(resultat1).isNotEqualTo(resultat2);

        resultat2.setId(resultat1.getId());
        assertThat(resultat1).isEqualTo(resultat2);

        resultat2 = getResultatSample2();
        assertThat(resultat1).isNotEqualTo(resultat2);
    }

    @Test
    void sessionTest() {
        Resultat resultat = getResultatRandomSampleGenerator();
        SessionTest sessionTestBack = getSessionTestRandomSampleGenerator();

        resultat.setSession(sessionTestBack);
        assertThat(resultat.getSession()).isEqualTo(sessionTestBack);
        assertThat(sessionTestBack.getResultat()).isEqualTo(resultat);

        resultat.session(null);
        assertThat(resultat.getSession()).isNull();
        assertThat(sessionTestBack.getResultat()).isNull();
    }
}
