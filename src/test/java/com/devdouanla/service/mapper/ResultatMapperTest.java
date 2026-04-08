package com.devdouanla.service.mapper;

import static com.devdouanla.domain.ResultatAsserts.*;
import static com.devdouanla.domain.ResultatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultatMapperTest {

    private ResultatMapper resultatMapper;

    @BeforeEach
    void setUp() {
        resultatMapper = new ResultatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResultatSample1();
        var actual = resultatMapper.toEntity(resultatMapper.toDto(expected));
        assertResultatAllPropertiesEquals(expected, actual);
    }
}
