package com.devdouanla.web.rest;

import com.devdouanla.repository.EpreuveRepository;
import com.devdouanla.service.EpreuveQueryService;
import com.devdouanla.service.EpreuveService;
import com.devdouanla.service.criteria.EpreuveCriteria;
import com.devdouanla.service.dto.EpreuveDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.devdouanla.domain.Epreuve}.
 */
@RestController
@RequestMapping("/api/epreuves")
public class EpreuveResource {

    private static final Logger LOG = LoggerFactory.getLogger(EpreuveResource.class);

    private static final String ENTITY_NAME = "epreuve";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final EpreuveService epreuveService;

    private final EpreuveRepository epreuveRepository;

    private final EpreuveQueryService epreuveQueryService;

    public EpreuveResource(EpreuveService epreuveService, EpreuveRepository epreuveRepository, EpreuveQueryService epreuveQueryService) {
        this.epreuveService = epreuveService;
        this.epreuveRepository = epreuveRepository;
        this.epreuveQueryService = epreuveQueryService;
    }

    /**
     * {@code POST  /epreuves} : Create a new epreuve.
     *
     * @param epreuveDTO the epreuveDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new epreuveDTO, or with status {@code 400 (Bad Request)} if the epreuve has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EpreuveDTO> createEpreuve(@Valid @RequestBody EpreuveDTO epreuveDTO) throws URISyntaxException {
        LOG.debug("REST request to save Epreuve : {}", epreuveDTO);
        if (epreuveDTO.getId() != null) {
            throw new BadRequestAlertException("A new epreuve cannot already have an ID", ENTITY_NAME, "idexists");
        }
        epreuveDTO = epreuveService.save(epreuveDTO);
        return ResponseEntity.created(new URI("/api/epreuves/" + epreuveDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, epreuveDTO.getId().toString()))
            .body(epreuveDTO);
    }

    /**
     * {@code PUT  /epreuves/:id} : Updates an existing epreuve.
     *
     * @param id the id of the epreuveDTO to save.
     * @param epreuveDTO the epreuveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epreuveDTO,
     * or with status {@code 400 (Bad Request)} if the epreuveDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the epreuveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EpreuveDTO> updateEpreuve(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EpreuveDTO epreuveDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Epreuve : {}, {}", id, epreuveDTO);
        if (epreuveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epreuveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epreuveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        epreuveDTO = epreuveService.update(epreuveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, epreuveDTO.getId().toString()))
            .body(epreuveDTO);
    }

    /**
     * {@code PATCH  /epreuves/:id} : Partial updates given fields of an existing epreuve, field will ignore if it is null
     *
     * @param id the id of the epreuveDTO to save.
     * @param epreuveDTO the epreuveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated epreuveDTO,
     * or with status {@code 400 (Bad Request)} if the epreuveDTO is not valid,
     * or with status {@code 404 (Not Found)} if the epreuveDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the epreuveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EpreuveDTO> partialUpdateEpreuve(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EpreuveDTO epreuveDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Epreuve partially : {}, {}", id, epreuveDTO);
        if (epreuveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, epreuveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!epreuveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EpreuveDTO> result = epreuveService.partialUpdate(epreuveDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, epreuveDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /epreuves} : get all the Epreuves.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Epreuves in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EpreuveDTO>> getAllEpreuves(
        EpreuveCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Epreuves by criteria: {}", criteria);

        Page<EpreuveDTO> page = epreuveQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /epreuves/count} : count all the epreuves.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEpreuves(EpreuveCriteria criteria) {
        LOG.debug("REST request to count Epreuves by criteria: {}", criteria);
        return ResponseEntity.ok().body(epreuveQueryService.countByCriteria(criteria));
    }





























    /**
     * {@code GET  /epreuves/:id} : get the "id" epreuve.
     *
     * @param id the id of the epreuveDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the epreuveDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EpreuveDTO> getEpreuve(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Epreuve : {}", id);
        Optional<EpreuveDTO> epreuveDTO = epreuveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(epreuveDTO);
    }

    /**
     * {@code DELETE  /epreuves/:id} : delete the "id" epreuve.
     *
     * @param id the id of the epreuveDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpreuve(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Epreuve : {}", id);
        epreuveService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    
     @GetMapping("/competence/{id}")
    public List<EpreuveDTO> getEpreuveByCompetenceId(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Epreuve by competence id : {}", id);
        return epreuveService.findByCompetenceId(id);
     }


}
