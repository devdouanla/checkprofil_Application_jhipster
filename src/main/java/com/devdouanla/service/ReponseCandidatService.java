package com.devdouanla.service;

import com.devdouanla.service.dto.ReponseCandidatDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.devdouanla.domain.ReponseCandidat}.
 */
public interface ReponseCandidatService {
    /**
     * Save a reponseCandidat.
     *
     * @param reponseCandidatDTO the entity to save.
     * @return the persisted entity.
     */
    ReponseCandidatDTO save(ReponseCandidatDTO reponseCandidatDTO);

    /**
     * Updates a reponseCandidat.
     *
     * @param reponseCandidatDTO the entity to update.
     * @return the persisted entity.
     */
    ReponseCandidatDTO update(ReponseCandidatDTO reponseCandidatDTO);

    /**
     * Partially updates a reponseCandidat.
     *
     * @param reponseCandidatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReponseCandidatDTO> partialUpdate(ReponseCandidatDTO reponseCandidatDTO);

    /**
     * Get all the reponseCandidats.
     *
     * @return the list of entities.
     */
    List<ReponseCandidatDTO> findAll();

    /**
     * Get the "id" reponseCandidat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReponseCandidatDTO> findOne(Long id);

    /**
     * Delete the "id" reponseCandidat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
