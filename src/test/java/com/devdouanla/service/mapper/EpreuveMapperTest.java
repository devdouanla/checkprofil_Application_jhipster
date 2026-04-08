package com.devdouanla.service.mapper;

import static com.devdouanla.domain.EpreuveAsserts.*;
import static com.devdouanla.domain.EpreuveTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpreuveMapperTest {

    private EpreuveMapper epreuveMapper;

    @BeforeEach
    void setUp() {
        epreuveMapper = new EpreuveMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEpreuveSample1();
        var actual = epreuveMapper.toEntity(epreuveMapper.toDto(expected));
        assertEpreuveAllPropertiesEquals(expected, actual);
    }
}
