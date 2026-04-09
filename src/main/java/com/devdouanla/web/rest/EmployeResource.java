package com.devdouanla.web.rest;

import com.devdouanla.repository.EmployeRepository;
import com.devdouanla.service.EmployeQueryService;
import com.devdouanla.service.EmployeService;
import com.devdouanla.service.criteria.EmployeCriteria;
import com.devdouanla.service.dto.EmployeDTO;
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
 * REST controller for managing {@link com.devdouanla.domain.Employe}.
 */
@RestController
@RequestMapping("/api/employes")
public class EmployeResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeResource.class);

    private static final String ENTITY_NAME = "employe";

    @Value("${jhipster.clientApp.name:checkprofile}")
    private String applicationName;

    private final EmployeService employeService;

    private final EmployeRepository employeRepository;

    private final EmployeQueryService employeQueryService;

    public EmployeResource(EmployeService employeService, EmployeRepository employeRepository, EmployeQueryService employeQueryService) {
        this.employeService = employeService;
        this.employeRepository = employeRepository;
        this.employeQueryService = employeQueryService;
    }

    /**
     * {@code POST  /employes} : Create a new employe.
     *
     * @param employeDTO the employeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeDTO, or with status {@code 400 (Bad Request)} if the employe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeDTO> createEmploye(@Valid @RequestBody EmployeDTO employeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Employe : {}", employeDTO);
        if (employeDTO.getId() != null) {
            throw new BadRequestAlertException("A new employe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeDTO = employeService.save(employeDTO);
        return ResponseEntity.created(new URI("/api/employes/" + employeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeDTO.getId().toString()))
            .body(employeDTO);
    }

    /**
     * {@code PUT  /employes/:id} : Updates an existing employe.
     *
     * @param id the id of the employeDTO to save.
     * @param employeDTO the employeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeDTO,
     * or with status {@code 400 (Bad Request)} if the employeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeDTO> updateEmploye(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeDTO employeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Employe : {}, {}", id, employeDTO);
        if (employeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeDTO = employeService.update(employeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeDTO.getId().toString()))
            .body(employeDTO);
    }

    /**
     * {@code PATCH  /employes/:id} : Partial updates given fields of an existing employe, field will ignore if it is null
     *
     * @param id the id of the employeDTO to save.
     * @param employeDTO the employeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeDTO,
     * or with status {@code 400 (Bad Request)} if the employeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeDTO> partialUpdateEmploye(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeDTO employeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Employe partially : {}, {}", id, employeDTO);
        if (employeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeDTO> result = employeService.partialUpdate(employeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employes} : get all the Employes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Employes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeDTO>> getAllEmployes(
        EmployeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Employes by criteria: {}", criteria);

        Page<EmployeDTO> page = employeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employes/byManager/:managerId} : get employes under manager via poste.manager.
     */
    @GetMapping("/byManager/{managerId}")
    public ResponseEntity<List<EmployeDTO>> getEmployesByManager(
        @PathVariable Long managerId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Employes by managerId: {}", managerId);
        EmployeCriteria criteria = new EmployeCriteria();
        criteria.posteId().equals(managerId);
        Page<EmployeDTO> page = employeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employes/count} : count all the employes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployes(EmployeCriteria criteria) {
        LOG.debug("REST request to count Employes by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employes/:id} : get the "id" employe.
     *
     * @param id the id of the employeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeDTO> getEmploye(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Employe : {}", id);
        Optional<EmployeDTO> employeDTO = employeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeDTO);
    }

    /**
     * {@code DELETE  /employes/:id} : delete the "id" employe.
     *
     * @param id the id of the employeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Employe : {}", id);
        employeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
