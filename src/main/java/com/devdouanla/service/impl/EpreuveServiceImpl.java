package com.devdouanla.service.impl;

import com.devdouanla.domain.Epreuve;
import com.devdouanla.repository.EpreuveRepository;
import com.devdouanla.service.EpreuveService;
import com.devdouanla.service.dto.EpreuveDTO;
import com.devdouanla.service.mapper.EpreuveMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devdouanla.domain.Epreuve}.
 */
@Service
@Transactional
public class EpreuveServiceImpl implements EpreuveService {

    private static final Logger LOG = LoggerFactory.getLogger(EpreuveServiceImpl.class);

    private final EpreuveRepository epreuveRepository;

    private final EpreuveMapper epreuveMapper;

    public EpreuveServiceImpl(EpreuveRepository epreuveRepository, EpreuveMapper epreuveMapper) {
        this.epreuveRepository = epreuveRepository;
        this.epreuveMapper = epreuveMapper;
    }

    @Override
    public EpreuveDTO save(EpreuveDTO epreuveDTO) {
        LOG.debug("Request to save Epreuve : {}", epreuveDTO);
        Epreuve epreuve = epreuveMapper.toEntity(epreuveDTO);
        epreuve = epreuveRepository.save(epreuve);
        return epreuveMapper.toDto(epreuve);
    }

    @Override
    public EpreuveDTO update(EpreuveDTO epreuveDTO) {
        LOG.debug("Request to update Epreuve : {}", epreuveDTO);
        Epreuve epreuve = epreuveMapper.toEntity(epreuveDTO);
        epreuve = epreuveRepository.save(epreuve);
        return epreuveMapper.toDto(epreuve);
    }

    @Override
    public Optional<EpreuveDTO> partialUpdate(EpreuveDTO epreuveDTO) {
        LOG.debug("Request to partially update Epreuve : {}", epreuveDTO);

        return epreuveRepository
            .findById(epreuveDTO.getId())
            .map(existingEpreuve -> {
                epreuveMapper.partialUpdate(existingEpreuve, epreuveDTO);

                return existingEpreuve;
            })
            .map(epreuveRepository::save)
            .map(epreuveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EpreuveDTO> findOne(Long id) {
        LOG.debug("Request to get Epreuve : {}", id);
        return epreuveRepository.findById(id).map(epreuveMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Epreuve : {}", id);
        epreuveRepository.deleteById(id);
    }

    @Override
    public List<EpreuveDTO> findByCompetenceId(Long CompetenceId) {
        // TODO Auto-generated method stub
return epreuveRepository.findByCompetenceId(CompetenceId).stream().map(epreuveMapper::toDto).toList();
    }
}
