package com.devdouanla.domain;

import static com.devdouanla.domain.ManagerTestSamples.*;
import static com.devdouanla.domain.PosteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ManagerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manager.class);
        Manager manager1 = getManagerSample1();
        Manager manager2 = new Manager();
        assertThat(manager1).isNotEqualTo(manager2);

        manager2.setId(manager1.getId());
        assertThat(manager1).isEqualTo(manager2);

        manager2 = getManagerSample2();
        assertThat(manager1).isNotEqualTo(manager2);
    }

    @Test
    void postesTest() {
        Manager manager = getManagerRandomSampleGenerator();
        Poste posteBack = getPosteRandomSampleGenerator();

        manager.addPostes(posteBack);
        assertThat(manager.getPosteses()).containsOnly(posteBack);
        assertThat(posteBack.getManager()).isEqualTo(manager);

        manager.removePostes(posteBack);
        assertThat(manager.getPosteses()).doesNotContain(posteBack);
        assertThat(posteBack.getManager()).isNull();

        manager.posteses(new HashSet<>(Set.of(posteBack)));
        assertThat(manager.getPosteses()).containsOnly(posteBack);
        assertThat(posteBack.getManager()).isEqualTo(manager);

        manager.setPosteses(new HashSet<>());
        assertThat(manager.getPosteses()).doesNotContain(posteBack);
        assertThat(posteBack.getManager()).isNull();
    }
}
