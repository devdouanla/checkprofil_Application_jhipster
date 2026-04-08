package com.devdouanla.service;

import com.devdouanla.service.dto.RHDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.devdouanla.domain.RH}.
 */
public interface RHService {
    /**
     * Save a rH.
     *
     * @param rHDTO the entity to save.
     * @return the persisted entity.
     */
    RHDTO save(RHDTO rHDTO);

    /**
     * Updates a rH.
     *
     * @param rHDTO the entity to update.
     * @return the persisted entity.
     */
    RHDTO update(RHDTO rHDTO);

    /**
     * Partially updates a rH.
     *
     * @param rHDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RHDTO> partialUpdate(RHDTO rHDTO);

    /**
     * Get all the rHS.
     *
     * @return the list of entities.
     */
    List<RHDTO> findAll();

    /**
     * Get all the rHS with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RHDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" rH.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RHDTO> findOne(Long id);

    /**
     * Delete the "id" rH.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
