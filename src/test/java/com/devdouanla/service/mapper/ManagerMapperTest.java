package com.devdouanla.service.mapper;

import static com.devdouanla.domain.ManagerAsserts.*;
import static com.devdouanla.domain.ManagerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManagerMapperTest {

    private ManagerMapper managerMapper;

    @BeforeEach
    void setUp() {
        managerMapper = new ManagerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getManagerSample1();
        var actual = managerMapper.toEntity(managerMapper.toDto(expected));
        assertManagerAllPropertiesEquals(expected, actual);
    }
}
