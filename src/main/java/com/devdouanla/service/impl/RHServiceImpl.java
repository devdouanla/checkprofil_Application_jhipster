package com.devdouanla.service.impl;

import com.devdouanla.domain.RH;
import com.devdouanla.repository.RHRepository;
import com.devdouanla.service.RHService;
import com.devdouanla.service.dto.RHDTO;
import com.devdouanla.service.mapper.RHMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devdouanla.domain.RH}.
 */
@Service
@Transactional
public class RHServiceImpl implements RHService {

    private static final Logger LOG = LoggerFactory.getLogger(RHServiceImpl.class);

    private final RHRepository rHRepository;

    private final RHMapper rHMapper;

    public RHServiceImpl(RHRepository rHRepository, RHMapper rHMapper) {
        this.rHRepository = rHRepository;
        this.rHMapper = rHMapper;
    }

    @Override
    public RHDTO save(RHDTO rHDTO) {
        LOG.debug("Request to save RH : {}", rHDTO);
        RH rH = rHMapper.toEntity(rHDTO);
        rH = rHRepository.save(rH);
        return rHMapper.toDto(rH);
    }

    @Override
    public RHDTO update(RHDTO rHDTO) {
        LOG.debug("Request to update RH : {}", rHDTO);
        RH rH = rHMapper.toEntity(rHDTO);
        rH = rHRepository.save(rH);
        return rHMapper.toDto(rH);
    }

    @Override
    public Optional<RHDTO> partialUpdate(RHDTO rHDTO) {
        LOG.debug("Request to partially update RH : {}", rHDTO);

        return rHRepository
            .findById(rHDTO.getId())
            .map(existingRH -> {
                rHMapper.partialUpdate(existingRH, rHDTO);

                return existingRH;
            })
            .map(rHRepository::save)
            .map(rHMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RHDTO> findAll() {
        LOG.debug("Request to get all RHS");
        return rHRepository.findAll().stream().map(rHMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<RHDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rHRepository.findAllWithEagerRelationships(pageable).map(rHMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RHDTO> findOne(Long id) {
        LOG.debug("Request to get RH : {}", id);
        return rHRepository.findOneWithEagerRelationships(id).map(rHMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete RH : {}", id);
        rHRepository.deleteById(id);
    }
}
