package com.devdouanla.service;

import com.devdouanla.domain.*; // for static metamodels
import com.devdouanla.domain.SessionTest;
import com.devdouanla.repository.SessionTestRepository;
import com.devdouanla.service.criteria.SessionTestCriteria;
import com.devdouanla.service.dto.SessionTestDTO;
import com.devdouanla.service.mapper.SessionTestMapper;
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
 * Service for executing complex queries for {@link SessionTest} entities in the database.
 * The main input is a {@link SessionTestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SessionTestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SessionTestQueryService extends QueryService<SessionTest> {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTestQueryService.class);

    private final SessionTestRepository sessionTestRepository;

    private final SessionTestMapper sessionTestMapper;

    public SessionTestQueryService(SessionTestRepository sessionTestRepository, SessionTestMapper sessionTestMapper) {
        this.sessionTestRepository = sessionTestRepository;
        this.sessionTestMapper = sessionTestMapper;
    }

    /**
     * Return a {@link Page} of {@link SessionTestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SessionTestDTO> findByCriteria(SessionTestCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SessionTest> specification = createSpecification(criteria);
        return sessionTestRepository.findAll(specification, page).map(sessionTestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SessionTestCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SessionTest> specification = createSpecification(criteria);
        return sessionTestRepository.count(specification);
    }

    /**
     * Function to convert {@link SessionTestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SessionTest> createSpecification(SessionTestCriteria criteria) {
        Specification<SessionTest> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), SessionTest_.id),
                buildRangeSpecification(criteria.getScoreObtenu(), SessionTest_.scoreObtenu),
                buildRangeSpecification(criteria.getDateDebut(), SessionTest_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), SessionTest_.dateFin),
                buildSpecification(criteria.getResultatId(), root -> root.join(SessionTest_.resultat, JoinType.LEFT).get(Resultat_.id)),
                buildSpecification(criteria.getQuestionsAskId(), root ->
                    root.join(SessionTest_.questionsAsks, JoinType.LEFT).get(QuestionAsk_.id)
                ),
                buildSpecification(criteria.getReponsesId(), root ->
                    root.join(SessionTest_.reponseses, JoinType.LEFT).get(ReponseCandidat_.id)
                ),
                buildSpecification(criteria.getEvaluationId(), root ->
                    root.join(SessionTest_.evaluation, JoinType.LEFT).get(Evaluation_.id)
                ),
                buildSpecification(criteria.getEpreuveId(), root -> root.join(SessionTest_.epreuve, JoinType.LEFT).get(Epreuve_.id))
            );
        }
        return specification;
    }
}
