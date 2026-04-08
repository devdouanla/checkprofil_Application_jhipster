package com.devdouanla.service;

import com.devdouanla.service.dto.ManagerDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.devdouanla.domain.Manager}.
 */
public interface ManagerService {
    /**
     * Save a manager.
     *
     * @param managerDTO the entity to save.
     * @return the persisted entity.
     */
    ManagerDTO save(ManagerDTO managerDTO);

    /**
     * Updates a manager.
     *
     * @param managerDTO the entity to update.
     * @return the persisted entity.
     */
    ManagerDTO update(ManagerDTO managerDTO);

    /**
     * Partially updates a manager.
     *
     * @param managerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManagerDTO> partialUpdate(ManagerDTO managerDTO);

    /**
     * Get all the managers.
     *
     * @return the list of entities.
     */
    List<ManagerDTO> findAll();

    /**
     * Get all the managers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManagerDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" manager.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManagerDTO> findOne(Long id);

    /**
     * Delete the "id" manager.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
