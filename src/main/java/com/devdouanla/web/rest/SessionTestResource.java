package com.devdouanla.web.rest;

import com.devdouanla.repository.SessionTestRepository;
import com.devdouanla.service.SessionTestQueryService;
import com.devdouanla.service.SessionTestService;
import com.devdouanla.service.criteria.SessionTestCriteria;
import com.devdouanla.service.dto.SessionTestDTO;
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
 * REST controller for managing {@link com.devdouanla.domain.SessionTest}.
 */
@RestController
@RequestMapping("/api/session-tests")
public class SessionTestResource {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTestResource.class);

    private static final String ENTITY_NAME = "sessionTest";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final SessionTestService sessionTestService;

    private final SessionTestRepository sessionTestRepository;

    private final SessionTestQueryService sessionTestQueryService;

    public SessionTestResource(
        SessionTestService sessionTestService,
        SessionTestRepository sessionTestRepository,
        SessionTestQueryService sessionTestQueryService
    ) {
        this.sessionTestService = sessionTestService;
        this.sessionTestRepository = sessionTestRepository;
        this.sessionTestQueryService = sessionTestQueryService;
    }

    /**
     * {@code POST  /session-tests} : Create a new sessionTest.
     *
     * @param sessionTestDTO the sessionTestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionTestDTO, or with status {@code 400 (Bad Request)} if the sessionTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SessionTestDTO> createSessionTest(@Valid @RequestBody SessionTestDTO sessionTestDTO) throws URISyntaxException {
        LOG.debug("REST request to save SessionTest : {}", sessionTestDTO);
        if (sessionTestDTO.getId() != null) {
            throw new BadRequestAlertException("A new sessionTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sessionTestDTO = sessionTestService.save(sessionTestDTO);
        return ResponseEntity.created(new URI("/api/session-tests/" + sessionTestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sessionTestDTO.getId().toString()))
            .body(sessionTestDTO);
    }

    /**
     * {@code PUT  /session-tests/:id} : Updates an existing sessionTest.
     *
     * @param id the id of the sessionTestDTO to save.
     * @param sessionTestDTO the sessionTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionTestDTO,
     * or with status {@code 400 (Bad Request)} if the sessionTestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SessionTestDTO> updateSessionTest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SessionTestDTO sessionTestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SessionTest : {}, {}", id, sessionTestDTO);
        if (sessionTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sessionTestDTO = sessionTestService.update(sessionTestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionTestDTO.getId().toString()))
            .body(sessionTestDTO);
    }

    /**
     * {@code PATCH  /session-tests/:id} : Partial updates given fields of an existing sessionTest, field will ignore if it is null
     *
     * @param id the id of the sessionTestDTO to save.
     * @param sessionTestDTO the sessionTestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionTestDTO,
     * or with status {@code 400 (Bad Request)} if the sessionTestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sessionTestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionTestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SessionTestDTO> partialUpdateSessionTest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SessionTestDTO sessionTestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SessionTest partially : {}, {}", id, sessionTestDTO);
        if (sessionTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionTestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionTestDTO> result = sessionTestService.partialUpdate(sessionTestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionTestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /session-tests} : get all the Session Tests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Session Tests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SessionTestDTO>> getAllSessionTests(
        SessionTestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SessionTests by criteria: {}", criteria);

        Page<SessionTestDTO> page = sessionTestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /session-tests/count} : count all the sessionTests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSessionTests(SessionTestCriteria criteria) {
        LOG.debug("REST request to count SessionTests by criteria: {}", criteria);
        return ResponseEntity.ok().body(sessionTestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /session-tests/:id} : get the "id" sessionTest.
     *
     * @param id the id of the sessionTestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionTestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionTestDTO> getSessionTest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SessionTest : {}", id);
        Optional<SessionTestDTO> sessionTestDTO = sessionTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionTestDTO);
    }

    /**
     * {@code DELETE  /session-tests/:id} : delete the "id" sessionTest.
     *
     * @param id the id of the sessionTestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSessionTest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SessionTest : {}", id);
        sessionTestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
