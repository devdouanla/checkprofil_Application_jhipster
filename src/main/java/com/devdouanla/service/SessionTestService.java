package com.devdouanla.service;

import com.devdouanla.service.dto.SessionTestDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.devdouanla.domain.SessionTest}.
 */
public interface SessionTestService {
    /**
     * Save a sessionTest.
     *
     * @param sessionTestDTO the entity to save.
     * @return the persisted entity.
     */
    SessionTestDTO save(SessionTestDTO sessionTestDTO);

    /**
     * Updates a sessionTest.
     *
     * @param sessionTestDTO the entity to update.
     * @return the persisted entity.
     */
    SessionTestDTO update(SessionTestDTO sessionTestDTO);

    /**
     * Partially updates a sessionTest.
     *
     * @param sessionTestDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SessionTestDTO> partialUpdate(SessionTestDTO sessionTestDTO);

    /**
     * Get the "id" sessionTest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionTestDTO> findOne(Long id);

    /**
     * Delete the "id" sessionTest.
     *
     * @param id the id of the entity.
     */
    /**
     * Generate a new SessionTest with random questions filtered by epreuve criteria.
     *
     * @param evaluationId the evaluation ID.
     * @param epreuveId the epreuve ID (defines competence, difficulty, nbQuestions).
     * @return the generated SessionTestDTO with populated questionsAsks.
     */
    SessionTestDTO generateSessionTest(Long evaluationId, Long epreuveId);

    void delete(Long id);
}
