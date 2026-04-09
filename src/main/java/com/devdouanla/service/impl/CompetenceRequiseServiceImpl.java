    package com.devdouanla.service.impl;

    import com.devdouanla.domain.CompetenceRequise;
    import com.devdouanla.repository.CompetenceRequiseRepository;
    import com.devdouanla.service.CompetenceRequiseService;
    import com.devdouanla.service.dto.CompetenceRequiseDTO;
    import com.devdouanla.service.mapper.CompetenceRequiseMapper;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    /**
     * Service Implementation for managing {@link com.devdouanla.domain.CompetenceRequise}.
     */
    @Service
    @Transactional
    public class CompetenceRequiseServiceImpl implements CompetenceRequiseService {

        private static final Logger LOG = LoggerFactory.getLogger(CompetenceRequiseServiceImpl.class);

        private final CompetenceRequiseRepository competenceRequiseRepository;

        private final CompetenceRequiseMapper competenceRequiseMapper;

        public CompetenceRequiseServiceImpl(
            CompetenceRequiseRepository competenceRequiseRepository,
            CompetenceRequiseMapper competenceRequiseMapper
        ) {
            this.competenceRequiseRepository = competenceRequiseRepository;
            this.competenceRequiseMapper = competenceRequiseMapper;
        }

        @Override
        public CompetenceRequiseDTO save(CompetenceRequiseDTO competenceRequiseDTO) {
            LOG.debug("Request to save CompetenceRequise : {}", competenceRequiseDTO);
            CompetenceRequise competenceRequise = competenceRequiseMapper.toEntity(competenceRequiseDTO);
            competenceRequise = competenceRequiseRepository.save(competenceRequise);
            return competenceRequiseMapper.toDto(competenceRequise);
        }

        @Override
        public CompetenceRequiseDTO update(CompetenceRequiseDTO competenceRequiseDTO) {
            LOG.debug("Request to update CompetenceRequise : {}", competenceRequiseDTO);
            CompetenceRequise competenceRequise = competenceRequiseMapper.toEntity(competenceRequiseDTO);
            competenceRequise = competenceRequiseRepository.save(competenceRequise);
            return competenceRequiseMapper.toDto(competenceRequise);
        }

        @Override
        public Optional<CompetenceRequiseDTO> partialUpdate(CompetenceRequiseDTO competenceRequiseDTO) {
            LOG.debug("Request to partially update CompetenceRequise : {}", competenceRequiseDTO);

            return competenceRequiseRepository
                .findById(competenceRequiseDTO.getId())
                .map(existingCompetenceRequise -> {
                    competenceRequiseMapper.partialUpdate(existingCompetenceRequise, competenceRequiseDTO);

                    return existingCompetenceRequise;
                })
                .map(competenceRequiseRepository::save)
                .map(competenceRequiseMapper::toDto);
        }

        @Override
        @Transactional(readOnly = true)
        public List<CompetenceRequiseDTO> findAll() {
            LOG.debug("Request to get all CompetenceRequises");
            return competenceRequiseRepository
                .findAll()
                .stream()
                .map(competenceRequiseMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<CompetenceRequiseDTO> findOne(Long id) {
            LOG.debug("Request to get CompetenceRequise : {}", id);
            return competenceRequiseRepository.findById(id).map(competenceRequiseMapper::toDto);
        }

        @Override
        public void delete(Long id) {
            LOG.debug("Request to delete CompetenceRequise : {}", id);
            competenceRequiseRepository.deleteById(id);
        }

        @Override
        @Transactional(readOnly = true)
        public List<CompetenceRequiseDTO> findByPosteId(Long POSTE_ID) {
return competenceRequiseRepository
                .findByPosteId(POSTE_ID)
                .stream()
                .map(competenceRequiseMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        
        }
    }