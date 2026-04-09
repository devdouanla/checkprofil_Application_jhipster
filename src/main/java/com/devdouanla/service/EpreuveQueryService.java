package com.devdouanla.service;

import com.devdouanla.domain.*; // for static metamodels
import com.devdouanla.domain.Epreuve;
import com.devdouanla.repository.EpreuveRepository;
import com.devdouanla.service.criteria.EpreuveCriteria;
import com.devdouanla.service.dto.EpreuveDTO;
import com.devdouanla.service.mapper.EpreuveMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Epreuve} entities in the database.
 * The main input is a {@link EpreuveCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EpreuveDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EpreuveQueryService extends QueryService<Epreuve> {

    private static final Logger LOG = LoggerFactory.getLogger(EpreuveQueryService.class);

    private final EpreuveRepository epreuveRepository;

    private final EpreuveMapper epreuveMapper;

    public EpreuveQueryService(EpreuveRepository epreuveRepository, EpreuveMapper epreuveMapper) {
        this.epreuveRepository = epreuveRepository;
        this.epreuveMapper = epreuveMapper;
    }

    /**
     * Return a {@link Page} of {@link EpreuveDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EpreuveDTO> findByCriteria(EpreuveCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Epreuve> specification = createSpecification(criteria);
        return epreuveRepository.findAll(specification, page).map(epreuveMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EpreuveCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Epreuve> specification = createSpecification(criteria);
        return epreuveRepository.count(specification);
    }

    /**
     * Function to convert {@link EpreuveCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Epreuve> createSpecification(EpreuveCriteria criteria) {
        Specification<Epreuve> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Epreuve_.id),
                buildStringSpecification(criteria.getTitre(), Epreuve_.titre),
                buildStringSpecification(criteria.getEnonce(), Epreuve_.enonce),
                buildSpecification(criteria.getDifficulte(), Epreuve_.difficulte),
                buildRangeSpecification(criteria.getDuree(), Epreuve_.duree),
                buildRangeSpecification(criteria.getNbQuestions(), Epreuve_.nbQuestions),
                buildSpecification(criteria.getGenereParIA(), Epreuve_.genereParIA),
                buildSpecification(criteria.getPublie(), Epreuve_.publie),
                buildSpecification(criteria.getSessionsId(), root -> root.join(Epreuve_.sessionses, JoinType.LEFT).get(SessionTest_.id)),
                buildSpecification(criteria.getCompetenceId(), root -> root.join(Epreuve_.competence, JoinType.LEFT).get(Competence_.id))
            );
        }
        return specification;
    }
}
