package com.devdouanla.service.impl;

import com.devdouanla.domain.SessionTest;
import com.devdouanla.repository.SessionTestRepository;
import com.devdouanla.service.SessionTestService;
import com.devdouanla.service.dto.SessionTestDTO;
import com.devdouanla.service.mapper.SessionTestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devdouanla.domain.SessionTest}.
 */
@Service
@Transactional
public class SessionTestServiceImpl implements SessionTestService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTestServiceImpl.class);

    private final SessionTestRepository sessionTestRepository;

    private final SessionTestMapper sessionTestMapper;

    public SessionTestServiceImpl(SessionTestRepository sessionTestRepository, SessionTestMapper sessionTestMapper) {
        this.sessionTestRepository = sessionTestRepository;
        this.sessionTestMapper = sessionTestMapper;
    }

    @Override
    public SessionTestDTO save(SessionTestDTO sessionTestDTO) {
        LOG.debug("Request to save SessionTest : {}", sessionTestDTO);
        SessionTest sessionTest = sessionTestMapper.toEntity(sessionTestDTO);
        sessionTest = sessionTestRepository.save(sessionTest);
        return sessionTestMapper.toDto(sessionTest);
    }

    @Override
    public SessionTestDTO update(SessionTestDTO sessionTestDTO) {
        LOG.debug("Request to update SessionTest : {}", sessionTestDTO);
        SessionTest sessionTest = sessionTestMapper.toEntity(sessionTestDTO);
        sessionTest = sessionTestRepository.save(sessionTest);
        return sessionTestMapper.toDto(sessionTest);
    }

    @Override
    public Optional<SessionTestDTO> partialUpdate(SessionTestDTO sessionTestDTO) {
        LOG.debug("Request to partially update SessionTest : {}", sessionTestDTO);

        return sessionTestRepository
            .findById(sessionTestDTO.getId())
            .map(existingSessionTest -> {
                sessionTestMapper.partialUpdate(existingSessionTest, sessionTestDTO);

                return existingSessionTest;
            })
            .map(sessionTestRepository::save)
            .map(sessionTestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionTestDTO> findOne(Long id) {
        LOG.debug("Request to get SessionTest : {}", id);
        return sessionTestRepository.findById(id).map(sessionTestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SessionTest : {}", id);
        sessionTestRepository.deleteById(id);
    }
}
