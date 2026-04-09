package com.devdouanla.service;

import com.devdouanla.domain.*; // for static metamodels
import com.devdouanla.domain.Competence;
import com.devdouanla.repository.CompetenceRepository;
import com.devdouanla.service.criteria.CompetenceCriteria;
import com.devdouanla.service.dto.CompetenceDTO;
import com.devdouanla.service.mapper.CompetenceMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Competence} entities in the database.
 * The main input is a {@link CompetenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompetenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompetenceQueryService extends QueryService<Competence> {

    private static final Logger LOG = LoggerFactory.getLogger(CompetenceQueryService.class);

    private final CompetenceRepository competenceRepository;

    private final CompetenceMapper competenceMapper;

    public CompetenceQueryService(CompetenceRepository competenceRepository, CompetenceMapper competenceMapper) {
        this.competenceRepository = competenceRepository;
        this.competenceMapper = competenceMapper;
    }

    /**
     * Return a {@link List} of {@link CompetenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompetenceDTO> findByCriteria(CompetenceCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Competence> specification = createSpecification(criteria);
        return competenceMapper.toDto(competenceRepository.fetchBagRelationships(competenceRepository.findAll(specification)));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompetenceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Competence> specification = createSpecification(criteria);
        return competenceRepository.count(specification);
    }

    /**
     * Function to convert {@link CompetenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Competence> createSpecification(CompetenceCriteria criteria) {
        Specification<Competence> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Competence_.id),
                buildStringSpecification(criteria.getNom(), Competence_.nom),
                buildSpecification(criteria.getQuestionsId(), root -> root.join(Competence_.questionses, JoinType.LEFT).get(Question_.id)),
                buildSpecification(criteria.getExpertsId(), root -> root.join(Competence_.expertses, JoinType.LEFT).get(Expert_.id)),
                buildSpecification(criteria.getEpreuvesId(), root -> root.join(Competence_.epreuveses, JoinType.LEFT).get(Epreuve_.id))
            );
        }
        return specification;
    }
}
