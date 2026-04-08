package com.devdouanla.service.mapper;

import static com.devdouanla.domain.SessionTestAsserts.*;
import static com.devdouanla.domain.SessionTestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SessionTestMapperTest {

    private SessionTestMapper sessionTestMapper;

    @BeforeEach
    void setUp() {
        sessionTestMapper = new SessionTestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSessionTestSample1();
        var actual = sessionTestMapper.toEntity(sessionTestMapper.toDto(expected));
        assertSessionTestAllPropertiesEquals(expected, actual);
    }
}
