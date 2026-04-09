package com.devdouanla.service;

import com.devdouanla.service.dto.EpreuveDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.devdouanla.domain.Epreuve}.
 */
public interface EpreuveService {
    /**
     * Save a epreuve.
     *
     * @param epreuveDTO the entity to save.
     * @return the persisted entity.
     */
    EpreuveDTO save(EpreuveDTO epreuveDTO);

    /**
     * Updates a epreuve.
     *
     * @param epreuveDTO the entity to update.
     * @return the persisted entity.
     */
    EpreuveDTO update(EpreuveDTO epreuveDTO);

    /**
     * Partially updates a epreuve.
     *
     * @param epreuveDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EpreuveDTO> partialUpdate(EpreuveDTO epreuveDTO);

    /**
     * Get the "id" epreuve.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EpreuveDTO> findOne(Long id);

    /**
     * Delete the "id" epreuve.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
           List<EpreuveDTO> findByCompetenceId( Long CompetenceId   );
}
