package com.devdouanla.service.mapper;

import static com.devdouanla.domain.CompetenceRequiseAsserts.*;
import static com.devdouanla.domain.CompetenceRequiseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompetenceRequiseMapperTest {

    private CompetenceRequiseMapper competenceRequiseMapper;

    @BeforeEach
    void setUp() {
        competenceRequiseMapper = new CompetenceRequiseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompetenceRequiseSample1();
        var actual = competenceRequiseMapper.toEntity(competenceRequiseMapper.toDto(expected));
        assertCompetenceRequiseAllPropertiesEquals(expected, actual);
    }
}
