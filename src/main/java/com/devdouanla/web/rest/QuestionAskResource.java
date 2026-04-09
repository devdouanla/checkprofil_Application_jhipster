package com.devdouanla.web.rest;

import com.devdouanla.repository.QuestionAskRepository;
import com.devdouanla.service.QuestionAskService;
import com.devdouanla.service.dto.QuestionAskDTO;
import com.devdouanla.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.devdouanla.domain.QuestionAsk}.
 */
@RestController
@RequestMapping("/api/question-asks")
public class QuestionAskResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionAskResource.class);

    private static final String ENTITY_NAME = "questionAsk";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final QuestionAskService questionAskService;

    private final QuestionAskRepository questionAskRepository;

    public QuestionAskResource(QuestionAskService questionAskService, QuestionAskRepository questionAskRepository) {
        this.questionAskService = questionAskService;
        this.questionAskRepository = questionAskRepository;
    }

    /**
     * {@code POST  /question-asks} : Create a new questionAsk.
     *
     * @param questionAskDTO the questionAskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionAskDTO, or with status {@code 400 (Bad Request)} if the questionAsk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionAskDTO> createQuestionAsk(@RequestBody QuestionAskDTO questionAskDTO) throws URISyntaxException {
        LOG.debug("REST request to save QuestionAsk : {}", questionAskDTO);
        if (questionAskDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionAsk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionAskDTO = questionAskService.save(questionAskDTO);
        return ResponseEntity.created(new URI("/api/question-asks/" + questionAskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, questionAskDTO.getId().toString()))
            .body(questionAskDTO);
    }

    /**
     * {@code PUT  /question-asks/:id} : Updates an existing questionAsk.
     *
     * @param id the id of the questionAskDTO to save.
     * @param questionAskDTO the questionAskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionAskDTO,
     * or with status {@code 400 (Bad Request)} if the questionAskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionAskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionAskDTO> updateQuestionAsk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionAskDTO questionAskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update QuestionAsk : {}, {}", id, questionAskDTO);
        if (questionAskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionAskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionAskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionAskDTO = questionAskService.update(questionAskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionAskDTO.getId().toString()))
            .body(questionAskDTO);
    }

    /**
     * {@code PATCH  /question-asks/:id} : Partial updates given fields of an existing questionAsk, field will ignore if it is null
     *
     * @param id the id of the questionAskDTO to save.
     * @param questionAskDTO the questionAskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionAskDTO,
     * or with status {@code 400 (Bad Request)} if the questionAskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionAskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionAskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionAskDTO> partialUpdateQuestionAsk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionAskDTO questionAskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update QuestionAsk partially : {}, {}", id, questionAskDTO);
        if (questionAskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionAskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionAskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionAskDTO> result = questionAskService.partialUpdate(questionAskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionAskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-asks} : get all the Question Asks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Question Asks in body.
     */
    @GetMapping("")
    public List<QuestionAskDTO> getAllQuestionAsks() {
        LOG.debug("REST request to get all QuestionAsks");
        return questionAskService.findAll();
    }

    /**
     * {@code GET  /question-asks/:id} : get the "id" questionAsk.
     *
     * @param id the id of the questionAskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionAskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionAskDTO> getQuestionAsk(@PathVariable("id") Long id) {
        LOG.debug("REST request to get QuestionAsk : {}", id);
        Optional<QuestionAskDTO> questionAskDTO = questionAskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionAskDTO);
    }

    /**
     * {@code DELETE  /question-asks/:id} : delete the "id" questionAsk.
     *
     * @param id the id of the questionAskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionAsk(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete QuestionAsk : {}", id);
        questionAskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
