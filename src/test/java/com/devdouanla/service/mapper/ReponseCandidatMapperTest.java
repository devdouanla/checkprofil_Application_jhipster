package com.devdouanla.service.mapper;

import static com.devdouanla.domain.ReponseCandidatAsserts.*;
import static com.devdouanla.domain.ReponseCandidatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReponseCandidatMapperTest {

    private ReponseCandidatMapper reponseCandidatMapper;

    @BeforeEach
    void setUp() {
        reponseCandidatMapper = new ReponseCandidatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReponseCandidatSample1();
        var actual = reponseCandidatMapper.toEntity(reponseCandidatMapper.toDto(expected));
        assertReponseCandidatAllPropertiesEquals(expected, actual);
    }
}
