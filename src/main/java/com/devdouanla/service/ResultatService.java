package com.devdouanla.service;

import com.devdouanla.service.dto.ResultatDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.devdouanla.domain.Resultat}.
 */
public interface ResultatService {
    /**
     * Save a resultat.
     *
     * @param resultatDTO the entity to save.
     * @return the persisted entity.
     */
    ResultatDTO save(ResultatDTO resultatDTO);

    /**
     * Updates a resultat.
     *
     * @param resultatDTO the entity to update.
     * @return the persisted entity.
     */
    ResultatDTO update(ResultatDTO resultatDTO);

    /**
     * Partially updates a resultat.
     *
     * @param resultatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ResultatDTO> partialUpdate(ResultatDTO resultatDTO);

    /**
     * Get all the resultats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResultatDTO> findAll(Pageable pageable);

    /**
     * Get all the ResultatDTO where Session is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ResultatDTO> findAllWhereSessionIsNull();

    /**
     * Get the "id" resultat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResultatDTO> findOne(Long id);

    /**
     * Delete the "id" resultat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
