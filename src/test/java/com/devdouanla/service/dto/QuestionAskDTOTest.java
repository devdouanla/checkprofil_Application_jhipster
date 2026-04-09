package com.devdouanla.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.devdouanla.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionAskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionAskDTO.class);
        QuestionAskDTO questionAskDTO1 = new QuestionAskDTO();
        questionAskDTO1.setId(1L);
        QuestionAskDTO questionAskDTO2 = new QuestionAskDTO();
        assertThat(questionAskDTO1).isNotEqualTo(questionAskDTO2);
        questionAskDTO2.setId(questionAskDTO1.getId());
        assertThat(questionAskDTO1).isEqualTo(questionAskDTO2);
        questionAskDTO2.setId(2L);
        assertThat(questionAskDTO1).isNotEqualTo(questionAskDTO2);
        questionAskDTO1.setId(null);
        assertThat(questionAskDTO1).isNotEqualTo(questionAskDTO2);
    }
}
