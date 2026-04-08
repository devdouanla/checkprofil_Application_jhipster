package com.devdouanla.web.rest;

import com.devdouanla.repository.RHRepository;
import com.devdouanla.service.RHService;
import com.devdouanla.service.dto.RHDTO;
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
 * REST controller for managing {@link com.devdouanla.domain.RH}.
 */
@RestController
@RequestMapping("/api/rhs")
public class RHResource {

    private static final Logger LOG = LoggerFactory.getLogger(RHResource.class);

    private static final String ENTITY_NAME = "rH";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final RHService rHService;

    private final RHRepository rHRepository;

    public RHResource(RHService rHService, RHRepository rHRepository) {
        this.rHService = rHService;
        this.rHRepository = rHRepository;
    }

    /**
     * {@code POST  /rhs} : Create a new rH.
     *
     * @param rHDTO the rHDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rHDTO, or with status {@code 400 (Bad Request)} if the rH has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RHDTO> createRH(@Valid @RequestBody RHDTO rHDTO) throws URISyntaxException {
        LOG.debug("REST request to save RH : {}", rHDTO);
        if (rHDTO.getId() != null) {
            throw new BadRequestAlertException("A new rH cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rHDTO = rHService.save(rHDTO);
        return ResponseEntity.created(new URI("/api/rhs/" + rHDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rHDTO.getId().toString()))
            .body(rHDTO);
    }

    /**
     * {@code PUT  /rhs/:id} : Updates an existing rH.
     *
     * @param id the id of the rHDTO to save.
     * @param rHDTO the rHDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rHDTO,
     * or with status {@code 400 (Bad Request)} if the rHDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rHDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RHDTO> updateRH(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody RHDTO rHDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update RH : {}, {}", id, rHDTO);
        if (rHDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rHDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rHRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rHDTO = rHService.update(rHDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rHDTO.getId().toString()))
            .body(rHDTO);
    }

    /**
     * {@code PATCH  /rhs/:id} : Partial updates given fields of an existing rH, field will ignore if it is null
     *
     * @param id the id of the rHDTO to save.
     * @param rHDTO the rHDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rHDTO,
     * or with status {@code 400 (Bad Request)} if the rHDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rHDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rHDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RHDTO> partialUpdateRH(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RHDTO rHDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RH partially : {}, {}", id, rHDTO);
        if (rHDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rHDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rHRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RHDTO> result = rHService.partialUpdate(rHDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rHDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rhs} : get all the RHS.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of RHS in body.
     */
    @GetMapping("")
    public List<RHDTO> getAllRHS(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all RHS");
        return rHService.findAll();
    }

    /**
     * {@code GET  /rhs/:id} : get the "id" rH.
     *
     * @param id the id of the rHDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rHDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RHDTO> getRH(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RH : {}", id);
        Optional<RHDTO> rHDTO = rHService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rHDTO);
    }

    /**
     * {@code DELETE  /rhs/:id} : delete the "id" rH.
     *
     * @param id the id of the rHDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRH(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RH : {}", id);
        rHService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
