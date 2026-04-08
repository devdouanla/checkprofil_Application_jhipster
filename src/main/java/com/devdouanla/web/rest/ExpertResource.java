package com.devdouanla.web.rest;

import com.devdouanla.repository.ExpertRepository;
import com.devdouanla.service.ExpertService;
import com.devdouanla.service.dto.ExpertDTO;
import com.devdouanla.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.devdouanla.domain.Expert}.
 */
@RestController
@RequestMapping("/api/experts")
public class ExpertResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExpertResource.class);

    private static final String ENTITY_NAME = "expert";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final ExpertService expertService;

    private final ExpertRepository expertRepository;

    public ExpertResource(ExpertService expertService, ExpertRepository expertRepository) {
        this.expertService = expertService;
        this.expertRepository = expertRepository;
    }

    /**
     * {@code POST  /experts} : Create a new expert.
     *
     * @param expertDTO the expertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expertDTO, or with status {@code 400 (Bad Request)} if the expert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExpertDTO> createExpert(@Valid @RequestBody ExpertDTO expertDTO) throws URISyntaxException {
        LOG.debug("REST request to save Expert : {}", expertDTO);
        if (expertDTO.getId() != null) {
            throw new BadRequestAlertException("A new expert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        expertDTO = expertService.save(expertDTO);
        return ResponseEntity.created(new URI("/api/experts/" + expertDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, expertDTO.getId().toString()))
            .body(expertDTO);
    }

    /**
     * {@code PUT  /experts/:id} : Updates an existing expert.
     *
     * @param id the id of the expertDTO to save.
     * @param expertDTO the expertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertDTO,
     * or with status {@code 400 (Bad Request)} if the expertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpertDTO> updateExpert(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExpertDTO expertDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Expert : {}, {}", id, expertDTO);
        if (expertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        expertDTO = expertService.update(expertDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expertDTO.getId().toString()))
            .body(expertDTO);
    }

    /**
     * {@code PATCH  /experts/:id} : Partial updates given fields of an existing expert, field will ignore if it is null
     *
     * @param id the id of the expertDTO to save.
     * @param expertDTO the expertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertDTO,
     * or with status {@code 400 (Bad Request)} if the expertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the expertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the expertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpertDTO> partialUpdateExpert(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExpertDTO expertDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Expert partially : {}, {}", id, expertDTO);
        if (expertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpertDTO> result = expertService.partialUpdate(expertDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expertDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /experts} : get all the Experts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Experts in body.
     */
    @GetMapping("")
    public List<ExpertDTO> getAllExperts(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Experts");
        return expertService.findAll();
    }

    /**
     * {@code GET  /experts/:id} : get the "id" expert.
     *
     * @param id the id of the expertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpertDTO> getExpert(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Expert : {}", id);
        Optional<ExpertDTO> expertDTO = expertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expertDTO);
    }

    /**
     * {@code DELETE  /experts/:id} : delete the "id" expert.
     *
     * @param id the id of the expertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpert(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Expert : {}", id);
        expertService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
