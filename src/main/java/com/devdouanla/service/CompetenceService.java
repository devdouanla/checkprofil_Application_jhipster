package com.devdouanla.service;

import com.devdouanla.service.dto.CompetenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.devdouanla.domain.Competence}.
 */
public interface CompetenceService {
    /**
     * Save a competence.
     *
     * @param competenceDTO the entity to save.
     * @return the persisted entity.
     */
    CompetenceDTO save(CompetenceDTO competenceDTO);

    /**
     * Updates a competence.
     *
     * @param competenceDTO the entity to update.
     * @return the persisted entity.
     */
    CompetenceDTO update(CompetenceDTO competenceDTO);

    /**
     * Partially updates a competence.
     *
     * @param competenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompetenceDTO> partialUpdate(CompetenceDTO competenceDTO);

    /**
     * Get all the competences with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompetenceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" competence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompetenceDTO> findOne(Long id);

    /**
     * Delete the "id" competence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
