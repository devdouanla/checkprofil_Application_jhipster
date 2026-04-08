package com.devdouanla.service.mapper;

import static com.devdouanla.domain.ExpertAsserts.*;
import static com.devdouanla.domain.ExpertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpertMapperTest {

    private ExpertMapper expertMapper;

    @BeforeEach
    void setUp() {
        expertMapper = new ExpertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExpertSample1();
        var actual = expertMapper.toEntity(expertMapper.toDto(expected));
        assertExpertAllPropertiesEquals(expected, actual);
    }
}
