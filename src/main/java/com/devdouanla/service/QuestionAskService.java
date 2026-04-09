package com.devdouanla.service;

import com.devdouanla.service.dto.QuestionAskDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.devdouanla.domain.QuestionAsk}.
 */
public interface QuestionAskService {
    /**
     * Save a questionAsk.
     *
     * @param questionAskDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionAskDTO save(QuestionAskDTO questionAskDTO);

    /**
     * Updates a questionAsk.
     *
     * @param questionAskDTO the entity to update.
     * @return the persisted entity.
     */
    QuestionAskDTO update(QuestionAskDTO questionAskDTO);

    /**
     * Partially updates a questionAsk.
     *
     * @param questionAskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionAskDTO> partialUpdate(QuestionAskDTO questionAskDTO);

    /**
     * Get all the questionAsks.
     *
     * @return the list of entities.
     */
    List<QuestionAskDTO> findAll();

    /**
     * Get the "id" questionAsk.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionAskDTO> findOne(Long id);

    /**
     * Delete the "id" questionAsk.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
