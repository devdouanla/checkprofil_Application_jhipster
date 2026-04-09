package com.devdouanla.service.mapper;

import static com.devdouanla.domain.QuestionAskAsserts.*;
import static com.devdouanla.domain.QuestionAskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionAskMapperTest {

    private QuestionAskMapper questionAskMapper;

    @BeforeEach
    void setUp() {
        questionAskMapper = new QuestionAskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuestionAskSample1();
        var actual = questionAskMapper.toEntity(questionAskMapper.toDto(expected));
        assertQuestionAskAllPropertiesEquals(expected, actual);
    }
}
