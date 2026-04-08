package com.devdouanla.service.impl;

import com.devdouanla.domain.ReponseCandidat;
import com.devdouanla.repository.ReponseCandidatRepository;
import com.devdouanla.service.ReponseCandidatService;
import com.devdouanla.service.dto.ReponseCandidatDTO;
import com.devdouanla.service.mapper.ReponseCandidatMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devdouanla.domain.ReponseCandidat}.
 */
@Service
@Transactional
public class ReponseCandidatServiceImpl implements ReponseCandidatService {

    private static final Logger LOG = LoggerFactory.getLogger(ReponseCandidatServiceImpl.class);

    private final ReponseCandidatRepository reponseCandidatRepository;

    private final ReponseCandidatMapper reponseCandidatMapper;

    public ReponseCandidatServiceImpl(ReponseCandidatRepository reponseCandidatRepository, ReponseCandidatMapper reponseCandidatMapper) {
        this.reponseCandidatRepository = reponseCandidatRepository;
        this.reponseCandidatMapper = reponseCandidatMapper;
    }

    @Override
    public ReponseCandidatDTO save(ReponseCandidatDTO reponseCandidatDTO) {
        LOG.debug("Request to save ReponseCandidat : {}", reponseCandidatDTO);
        ReponseCandidat reponseCandidat = reponseCandidatMapper.toEntity(reponseCandidatDTO);
        reponseCandidat = reponseCandidatRepository.save(reponseCandidat);
        return reponseCandidatMapper.toDto(reponseCandidat);
    }

    @Override
    public ReponseCandidatDTO update(ReponseCandidatDTO reponseCandidatDTO) {
        LOG.debug("Request to update ReponseCandidat : {}", reponseCandidatDTO);
        ReponseCandidat reponseCandidat = reponseCandidatMapper.toEntity(reponseCandidatDTO);
        reponseCandidat = reponseCandidatRepository.save(reponseCandidat);
        return reponseCandidatMapper.toDto(reponseCandidat);
    }

    @Override
    public Optional<ReponseCandidatDTO> partialUpdate(ReponseCandidatDTO reponseCandidatDTO) {
        LOG.debug("Request to partially update ReponseCandidat : {}", reponseCandidatDTO);

        return reponseCandidatRepository
            .findById(reponseCandidatDTO.getId())
            .map(existingReponseCandidat -> {
                reponseCandidatMapper.partialUpdate(existingReponseCandidat, reponseCandidatDTO);

                return existingReponseCandidat;
            })
            .map(reponseCandidatRepository::save)
            .map(reponseCandidatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReponseCandidatDTO> findAll() {
        LOG.debug("Request to get all ReponseCandidats");
        return reponseCandidatRepository
            .findAll()
            .stream()
            .map(reponseCandidatMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReponseCandidatDTO> findOne(Long id) {
        LOG.debug("Request to get ReponseCandidat : {}", id);
        return reponseCandidatRepository.findById(id).map(reponseCandidatMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReponseCandidat : {}", id);
        reponseCandidatRepository.deleteById(id);
    }
}
