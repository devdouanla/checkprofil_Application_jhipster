package com.devdouanla.service;

import com.devdouanla.service.dto.ExpertDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.devdouanla.domain.Expert}.
 */
public interface ExpertService {
    /**
     * Save a expert.
     *
     * @param expertDTO the entity to save.
     * @return the persisted entity.
     */
    ExpertDTO save(ExpertDTO expertDTO);

    /**
     * Updates a expert.
     *
     * @param expertDTO the entity to update.
     * @return the persisted entity.
     */
    ExpertDTO update(ExpertDTO expertDTO);

    /**
     * Partially updates a expert.
     *
     * @param expertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExpertDTO> partialUpdate(ExpertDTO expertDTO);

    /**
     * Get all the experts.
     *
     * @return the list of entities.
     */
    List<ExpertDTO> findAll();

    /**
     * Get all the experts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpertDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" expert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpertDTO> findOne(Long id);

    /**
     * Delete the "id" expert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
