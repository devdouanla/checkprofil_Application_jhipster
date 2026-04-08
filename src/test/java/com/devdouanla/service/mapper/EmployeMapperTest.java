package com.devdouanla.service.mapper;

import static com.devdouanla.domain.EmployeAsserts.*;
import static com.devdouanla.domain.EmployeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeMapperTest {

    private EmployeMapper employeMapper;

    @BeforeEach
    void setUp() {
        employeMapper = new EmployeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmployeSample1();
        var actual = employeMapper.toEntity(employeMapper.toDto(expected));
        assertEmployeAllPropertiesEquals(expected, actual);
    }
}
