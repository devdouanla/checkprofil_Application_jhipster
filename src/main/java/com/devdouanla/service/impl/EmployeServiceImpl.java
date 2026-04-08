package com.devdouanla.service.impl;

import com.devdouanla.domain.Employe;
import com.devdouanla.repository.EmployeRepository;
import com.devdouanla.service.EmployeService;
import com.devdouanla.service.dto.EmployeDTO;
import com.devdouanla.service.mapper.EmployeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devdouanla.domain.Employe}.
 */
@Service
@Transactional
public class EmployeServiceImpl implements EmployeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeServiceImpl.class);

    private final EmployeRepository employeRepository;

    private final EmployeMapper employeMapper;

    public EmployeServiceImpl(EmployeRepository employeRepository, EmployeMapper employeMapper) {
        this.employeRepository = employeRepository;
        this.employeMapper = employeMapper;
    }

    @Override
    public EmployeDTO save(EmployeDTO employeDTO) {
        LOG.debug("Request to save Employe : {}", employeDTO);
        Employe employe = employeMapper.toEntity(employeDTO);
        employe = employeRepository.save(employe);
        return employeMapper.toDto(employe);
    }

    @Override
    public EmployeDTO update(EmployeDTO employeDTO) {
        LOG.debug("Request to update Employe : {}", employeDTO);
        Employe employe = employeMapper.toEntity(employeDTO);
        employe = employeRepository.save(employe);
        return employeMapper.toDto(employe);
    }

    @Override
    public Optional<EmployeDTO> partialUpdate(EmployeDTO employeDTO) {
        LOG.debug("Request to partially update Employe : {}", employeDTO);

        return employeRepository
            .findById(employeDTO.getId())
            .map(existingEmploye -> {
                employeMapper.partialUpdate(existingEmploye, employeDTO);

                return existingEmploye;
            })
            .map(employeRepository::save)
            .map(employeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeDTO> findOne(Long id) {
        LOG.debug("Request to get Employe : {}", id);
        return employeRepository.findById(id).map(employeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Employe : {}", id);
        employeRepository.deleteById(id);
    }
}
