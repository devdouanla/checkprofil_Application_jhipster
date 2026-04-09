package com.devdouanla.service;

import com.devdouanla.service.dto.CompetenceRequiseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.devdouanla.domain.CompetenceRequise}.
 */
public interface CompetenceRequiseService {
    /**
     * Save a competenceRequise.
     *
     * @param competenceRequiseDTO the entity to save.
     * @return the persisted entity.
     */
    CompetenceRequiseDTO save(CompetenceRequiseDTO competenceRequiseDTO);

    /**
     * Updates a competenceRequise.
     *
     * @param competenceRequiseDTO the entity to update.
     * @return the persisted entity.
     */
    CompetenceRequiseDTO update(CompetenceRequiseDTO competenceRequiseDTO);

    /**
     * Partially updates a competenceRequise.
     *
     * @param competenceRequiseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompetenceRequiseDTO> partialUpdate(CompetenceRequiseDTO competenceRequiseDTO);

    /**
     * Get all the competenceRequises.
     *
     * @return the list of entities.
     */
    List<CompetenceRequiseDTO> findAll();
   List<CompetenceRequiseDTO> findByPosteId( Long POSTE_ID);

    /**
     * Get the "id" competenceRequise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompetenceRequiseDTO> findOne(Long id);

    /**
     * Delete the "id" competenceRequise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}