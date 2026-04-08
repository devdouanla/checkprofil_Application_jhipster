package com.devdouanla.service.mapper;

import static com.devdouanla.domain.RHAsserts.*;
import static com.devdouanla.domain.RHTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RHMapperTest {

    private RHMapper rHMapper;

    @BeforeEach
    void setUp() {
        rHMapper = new RHMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRHSample1();
        var actual = rHMapper.toEntity(rHMapper.toDto(expected));
        assertRHAllPropertiesEquals(expected, actual);
    }
}
