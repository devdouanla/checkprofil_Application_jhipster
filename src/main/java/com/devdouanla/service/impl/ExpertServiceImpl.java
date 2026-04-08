package com.devdouanla.service.impl;

import com.devdouanla.domain.Expert;
import com.devdouanla.repository.ExpertRepository;
import com.devdouanla.service.ExpertService;
import com.devdouanla.service.dto.ExpertDTO;
import com.devdouanla.service.mapper.ExpertMapper;
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
 * Service Implementation for managing {@link com.devdouanla.domain.Expert}.
 */
@Service
@Transactional
public class ExpertServiceImpl implements ExpertService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpertServiceImpl.class);

    private final ExpertRepository expertRepository;

    private final ExpertMapper expertMapper;

    public ExpertServiceImpl(ExpertRepository expertRepository, ExpertMapper expertMapper) {
        this.expertRepository = expertRepository;
        this.expertMapper = expertMapper;
    }

    @Override
    public ExpertDTO save(ExpertDTO expertDTO) {
        LOG.debug("Request to save Expert : {}", expertDTO);
        Expert expert = expertMapper.toEntity(expertDTO);
        expert = expertRepository.save(expert);
        return expertMapper.toDto(expert);
    }

    @Override
    public ExpertDTO update(ExpertDTO expertDTO) {
        LOG.debug("Request to update Expert : {}", expertDTO);
        Expert expert = expertMapper.toEntity(expertDTO);
        expert = expertRepository.save(expert);
        return expertMapper.toDto(expert);
    }

    @Override
    public Optional<ExpertDTO> partialUpdate(ExpertDTO expertDTO) {
        LOG.debug("Request to partially update Expert : {}", expertDTO);

        return expertRepository
            .findById(expertDTO.getId())
            .map(existingExpert -> {
                expertMapper.partialUpdate(existingExpert, expertDTO);

                return existingExpert;
            })
            .map(expertRepository::save)
            .map(expertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpertDTO> findAll() {
        LOG.debug("Request to get all Experts");
        return expertRepository.findAll().stream().map(expertMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ExpertDTO> findAllWithEagerRelationships(Pageable pageable) {
        return expertRepository.findAllWithEagerRelationships(pageable).map(expertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExpertDTO> findOne(Long id) {
        LOG.debug("Request to get Expert : {}", id);
        return expertRepository.findOneWithEagerRelationships(id).map(expertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Expert : {}", id);
        expertRepository.deleteById(id);
    }
}
