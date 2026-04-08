package com.devdouanla.service.impl;

import com.devdouanla.domain.Manager;
import com.devdouanla.repository.ManagerRepository;
import com.devdouanla.service.ManagerService;
import com.devdouanla.service.dto.ManagerDTO;
import com.devdouanla.service.mapper.ManagerMapper;
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
 * Service Implementation for managing {@link com.devdouanla.domain.Manager}.
 */
@Service
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(ManagerServiceImpl.class);

    private final ManagerRepository managerRepository;

    private final ManagerMapper managerMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    public ManagerDTO save(ManagerDTO managerDTO) {
        LOG.debug("Request to save Manager : {}", managerDTO);
        Manager manager = managerMapper.toEntity(managerDTO);
        manager = managerRepository.save(manager);
        return managerMapper.toDto(manager);
    }

    @Override
    public ManagerDTO update(ManagerDTO managerDTO) {
        LOG.debug("Request to update Manager : {}", managerDTO);
        Manager manager = managerMapper.toEntity(managerDTO);
        manager = managerRepository.save(manager);
        return managerMapper.toDto(manager);
    }

    @Override
    public Optional<ManagerDTO> partialUpdate(ManagerDTO managerDTO) {
        LOG.debug("Request to partially update Manager : {}", managerDTO);

        return managerRepository
            .findById(managerDTO.getId())
            .map(existingManager -> {
                managerMapper.partialUpdate(existingManager, managerDTO);

                return existingManager;
            })
            .map(managerRepository::save)
            .map(managerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> findAll() {
        LOG.debug("Request to get all Managers");
        return managerRepository.findAll().stream().map(managerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ManagerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return managerRepository.findAllWithEagerRelationships(pageable).map(managerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManagerDTO> findOne(Long id) {
        LOG.debug("Request to get Manager : {}", id);
        return managerRepository.findOneWithEagerRelationships(id).map(managerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Manager : {}", id);
        managerRepository.deleteById(id);
    }
}
