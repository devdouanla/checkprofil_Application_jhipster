package com.devdouanla.web.rest;

import com.devdouanla.repository.ReponseCandidatRepository;
import com.devdouanla.service.ReponseCandidatService;
import com.devdouanla.service.dto.ReponseCandidatDTO;
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
 * REST controller for managing {@link com.devdouanla.domain.ReponseCandidat}.
 */
@RestController
@RequestMapping("/api/reponse-candidats")
public class ReponseCandidatResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReponseCandidatResource.class);

    private static final String ENTITY_NAME = "reponseCandidat";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final ReponseCandidatService reponseCandidatService;

    private final ReponseCandidatRepository reponseCandidatRepository;

    public ReponseCandidatResource(ReponseCandidatService reponseCandidatService, ReponseCandidatRepository reponseCandidatRepository) {
        this.reponseCandidatService = reponseCandidatService;
        this.reponseCandidatRepository = reponseCandidatRepository;
    }

    /**
     * {@code POST  /reponse-candidats} : Create a new reponseCandidat.
     *
     * @param reponseCandidatDTO the reponseCandidatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reponseCandidatDTO, or with status {@code 400 (Bad Request)} if the reponseCandidat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReponseCandidatDTO> createReponseCandidat(@Valid @RequestBody ReponseCandidatDTO reponseCandidatDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReponseCandidat : {}", reponseCandidatDTO);
        if (reponseCandidatDTO.getId() != null) {
            throw new BadRequestAlertException("A new reponseCandidat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reponseCandidatDTO = reponseCandidatService.save(reponseCandidatDTO);
        return ResponseEntity.created(new URI("/api/reponse-candidats/" + reponseCandidatDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reponseCandidatDTO.getId().toString()))
            .body(reponseCandidatDTO);
    }

    /**
     * {@code PUT  /reponse-candidats/:id} : Updates an existing reponseCandidat.
     *
     * @param id the id of the reponseCandidatDTO to save.
     * @param reponseCandidatDTO the reponseCandidatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reponseCandidatDTO,
     * or with status {@code 400 (Bad Request)} if the reponseCandidatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reponseCandidatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReponseCandidatDTO> updateReponseCandidat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReponseCandidatDTO reponseCandidatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReponseCandidat : {}, {}", id, reponseCandidatDTO);
        if (reponseCandidatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reponseCandidatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reponseCandidatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reponseCandidatDTO = reponseCandidatService.update(reponseCandidatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reponseCandidatDTO.getId().toString()))
            .body(reponseCandidatDTO);
    }

    /**
     * {@code PATCH  /reponse-candidats/:id} : Partial updates given fields of an existing reponseCandidat, field will ignore if it is null
     *
     * @param id the id of the reponseCandidatDTO to save.
     * @param reponseCandidatDTO the reponseCandidatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reponseCandidatDTO,
     * or with status {@code 400 (Bad Request)} if the reponseCandidatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reponseCandidatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reponseCandidatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReponseCandidatDTO> partialUpdateReponseCandidat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReponseCandidatDTO reponseCandidatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReponseCandidat partially : {}, {}", id, reponseCandidatDTO);
        if (reponseCandidatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reponseCandidatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reponseCandidatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReponseCandidatDTO> result = reponseCandidatService.partialUpdate(reponseCandidatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reponseCandidatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reponse-candidats} : get all the Reponse Candidats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Reponse Candidats in body.
     */
    @GetMapping("")
    public List<ReponseCandidatDTO> getAllReponseCandidats() {
        LOG.debug("REST request to get all ReponseCandidats");
        return reponseCandidatService.findAll();
    }

    /**
     * {@code GET  /reponse-candidats/:id} : get the "id" reponseCandidat.
     *
     * @param id the id of the reponseCandidatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reponseCandidatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReponseCandidatDTO> getReponseCandidat(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReponseCandidat : {}", id);
        Optional<ReponseCandidatDTO> reponseCandidatDTO = reponseCandidatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reponseCandidatDTO);
    }

    /**
     * {@code DELETE  /reponse-candidats/:id} : delete the "id" reponseCandidat.
     *
     * @param id the id of the reponseCandidatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReponseCandidat(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReponseCandidat : {}", id);
        reponseCandidatService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
