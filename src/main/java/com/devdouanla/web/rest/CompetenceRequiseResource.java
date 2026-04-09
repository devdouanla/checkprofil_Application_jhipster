package com.devdouanla.web.rest;

import com.devdouanla.repository.CompetenceRequiseRepository;
import com.devdouanla.service.CompetenceRequiseService;
import com.devdouanla.service.dto.CompetenceRequiseDTO;
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
 * REST controller for managing {@link com.devdouanla.domain.CompetenceRequise}.
 */
@RestController
@RequestMapping("/api/competence-requises")
public class CompetenceRequiseResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompetenceRequiseResource.class);

    private static final String ENTITY_NAME = "competenceRequise";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final CompetenceRequiseService competenceRequiseService;

    private final CompetenceRequiseRepository competenceRequiseRepository;

    public CompetenceRequiseResource(
        CompetenceRequiseService competenceRequiseService,
        CompetenceRequiseRepository competenceRequiseRepository
    ) {
        this.competenceRequiseService = competenceRequiseService;
        this.competenceRequiseRepository = competenceRequiseRepository;
    }

    /**
     * {@code POST  /competence-requises} : Create a new competenceRequise.
     *
     * @param competenceRequiseDTO the competenceRequiseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competenceRequiseDTO, or with status {@code 400 (Bad Request)} if the competenceRequise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompetenceRequiseDTO> createCompetenceRequise(@Valid @RequestBody CompetenceRequiseDTO competenceRequiseDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CompetenceRequise : {}", competenceRequiseDTO);
        if (competenceRequiseDTO.getId() != null) {
            throw new BadRequestAlertException("A new competenceRequise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        competenceRequiseDTO = competenceRequiseService.save(competenceRequiseDTO);
        return ResponseEntity.created(new URI("/api/competence-requises/" + competenceRequiseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, competenceRequiseDTO.getId().toString()))
            .body(competenceRequiseDTO);
    }

    /**
     * {@code PUT  /competence-requises/:id} : Updates an existing competenceRequise.
     *
     * @param id the id of the competenceRequiseDTO to save.
     * @param competenceRequiseDTO the competenceRequiseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competenceRequiseDTO,
     * or with status {@code 400 (Bad Request)} if the competenceRequiseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competenceRequiseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompetenceRequiseDTO> updateCompetenceRequise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompetenceRequiseDTO competenceRequiseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CompetenceRequise : {}, {}", id, competenceRequiseDTO);
        if (competenceRequiseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competenceRequiseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competenceRequiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        competenceRequiseDTO = competenceRequiseService.update(competenceRequiseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competenceRequiseDTO.getId().toString()))
            .body(competenceRequiseDTO);
    }

    /**
     * {@code PATCH  /competence-requises/:id} : Partial updates given fields of an existing competenceRequise, field will ignore if it is null
     *
     * @param id the id of the competenceRequiseDTO to save.
     * @param competenceRequiseDTO the competenceRequiseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competenceRequiseDTO,
     * or with status {@code 400 (Bad Request)} if the competenceRequiseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the competenceRequiseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the competenceRequiseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompetenceRequiseDTO> partialUpdateCompetenceRequise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompetenceRequiseDTO competenceRequiseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CompetenceRequise partially : {}, {}", id, competenceRequiseDTO);
        if (competenceRequiseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competenceRequiseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competenceRequiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompetenceRequiseDTO> result = competenceRequiseService.partialUpdate(competenceRequiseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competenceRequiseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /competence-requises} : get all the Competence Requises.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Competence Requises in body.
     */
    @GetMapping("")
    public List<CompetenceRequiseDTO> getAllCompetenceRequises() {
        LOG.debug("REST request to get all CompetenceRequises");
        return competenceRequiseService.findAll();
    }

    /**
     * {@code GET  /competence-requises/:id} : get the "id" competenceRequise.
     *
     * @param id the id of the competenceRequiseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competenceRequiseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompetenceRequiseDTO> getCompetenceRequise(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CompetenceRequise : {}", id);
        Optional<CompetenceRequiseDTO> competenceRequiseDTO = competenceRequiseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(competenceRequiseDTO);
    }


     @GetMapping("poste/{id}")
    public List<CompetenceRequiseDTO> getALLCompetenceRequiseByPosteId(@PathVariable("id") Long id) {
        LOG.debug("REST request to get all CompetenceRequises by poste id : {}", id);
        return competenceRequiseService.findByPosteId(id);
    }

    /**
     * {@code DELETE  /competence-requises/:id} : delete the "id" competenceRequise.
     *
     * @param id the id of the competenceRequiseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetenceRequise(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CompetenceRequise : {}", id);
        competenceRequiseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}