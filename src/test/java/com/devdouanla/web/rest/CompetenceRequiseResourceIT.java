package com.devdouanla.web.rest;

import static com.devdouanla.domain.CompetenceRequiseAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.CompetenceRequise;
import com.devdouanla.repository.CompetenceRequiseRepository;
import com.devdouanla.service.dto.CompetenceRequiseDTO;
import com.devdouanla.service.mapper.CompetenceRequiseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompetenceRequiseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompetenceRequiseResourceIT {

    private static final Boolean DEFAULT_OBLIGATOIRE = false;
    private static final Boolean UPDATED_OBLIGATOIRE = true;

    private static final String ENTITY_API_URL = "/api/competence-requises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompetenceRequiseRepository competenceRequiseRepository;

    @Autowired
    private CompetenceRequiseMapper competenceRequiseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetenceRequiseMockMvc;

    private CompetenceRequise competenceRequise;

    private CompetenceRequise insertedCompetenceRequise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompetenceRequise createEntity() {
        return new CompetenceRequise().obligatoire(DEFAULT_OBLIGATOIRE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompetenceRequise createUpdatedEntity() {
        return new CompetenceRequise().obligatoire(UPDATED_OBLIGATOIRE);
    }

    @BeforeEach
    void initTest() {
        competenceRequise = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompetenceRequise != null) {
            competenceRequiseRepository.delete(insertedCompetenceRequise);
            insertedCompetenceRequise = null;
        }
    }

    @Test
    @Transactional
    void createCompetenceRequise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompetenceRequise
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);
        var returnedCompetenceRequiseDTO = om.readValue(
            restCompetenceRequiseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(competenceRequiseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompetenceRequiseDTO.class
        );

        // Validate the CompetenceRequise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompetenceRequise = competenceRequiseMapper.toEntity(returnedCompetenceRequiseDTO);
        assertCompetenceRequiseUpdatableFieldsEquals(returnedCompetenceRequise, getPersistedCompetenceRequise(returnedCompetenceRequise));

        insertedCompetenceRequise = returnedCompetenceRequise;
    }

    @Test
    @Transactional
    void createCompetenceRequiseWithExistingId() throws Exception {
        // Create the CompetenceRequise with an existing ID
        competenceRequise.setId(1L);
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetenceRequiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(competenceRequiseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkObligatoireIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        competenceRequise.setObligatoire(null);

        // Create the CompetenceRequise, which fails.
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        restCompetenceRequiseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(competenceRequiseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompetenceRequises() throws Exception {
        // Initialize the database
        insertedCompetenceRequise = competenceRequiseRepository.saveAndFlush(competenceRequise);

        // Get all the competenceRequiseList
        restCompetenceRequiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competenceRequise.getId().intValue())))
            .andExpect(jsonPath("$.[*].obligatoire").value(hasItem(DEFAULT_OBLIGATOIRE)));
    }

    @Test
    @Transactional
    void getCompetenceRequise() throws Exception {
        // Initialize the database
        insertedCompetenceRequise = competenceRequiseRepository.saveAndFlush(competenceRequise);

        // Get the competenceRequise
        restCompetenceRequiseMockMvc
            .perform(get(ENTITY_API_URL_ID, competenceRequise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competenceRequise.getId().intValue()))
            .andExpect(jsonPath("$.obligatoire").value(DEFAULT_OBLIGATOIRE));
    }

    @Test
    @Transactional
    void getNonExistingCompetenceRequise() throws Exception {
        // Get the competenceRequise
        restCompetenceRequiseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompetenceRequise() throws Exception {
        // Initialize the database
        insertedCompetenceRequise = competenceRequiseRepository.saveAndFlush(competenceRequise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the competenceRequise
        CompetenceRequise updatedCompetenceRequise = competenceRequiseRepository.findById(competenceRequise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompetenceRequise are not directly saved in db
        em.detach(updatedCompetenceRequise);
        updatedCompetenceRequise.obligatoire(UPDATED_OBLIGATOIRE);
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(updatedCompetenceRequise);

        restCompetenceRequiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competenceRequiseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(competenceRequiseDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompetenceRequiseToMatchAllProperties(updatedCompetenceRequise);
    }

    @Test
    @Transactional
    void putNonExistingCompetenceRequise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        competenceRequise.setId(longCount.incrementAndGet());

        // Create the CompetenceRequise
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetenceRequiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competenceRequiseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(competenceRequiseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompetenceRequise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        competenceRequise.setId(longCount.incrementAndGet());

        // Create the CompetenceRequise
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceRequiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(competenceRequiseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompetenceRequise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        competenceRequise.setId(longCount.incrementAndGet());

        // Create the CompetenceRequise
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceRequiseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(competenceRequiseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompetenceRequiseWithPatch() throws Exception {
        // Initialize the database
        insertedCompetenceRequise = competenceRequiseRepository.saveAndFlush(competenceRequise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the competenceRequise using partial update
        CompetenceRequise partialUpdatedCompetenceRequise = new CompetenceRequise();
        partialUpdatedCompetenceRequise.setId(competenceRequise.getId());

        partialUpdatedCompetenceRequise.obligatoire(UPDATED_OBLIGATOIRE);

        restCompetenceRequiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetenceRequise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompetenceRequise))
            )
            .andExpect(status().isOk());

        // Validate the CompetenceRequise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompetenceRequiseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompetenceRequise, competenceRequise),
            getPersistedCompetenceRequise(competenceRequise)
        );
    }

    @Test
    @Transactional
    void fullUpdateCompetenceRequiseWithPatch() throws Exception {
        // Initialize the database
        insertedCompetenceRequise = competenceRequiseRepository.saveAndFlush(competenceRequise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the competenceRequise using partial update
        CompetenceRequise partialUpdatedCompetenceRequise = new CompetenceRequise();
        partialUpdatedCompetenceRequise.setId(competenceRequise.getId());

        partialUpdatedCompetenceRequise.obligatoire(UPDATED_OBLIGATOIRE);

        restCompetenceRequiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetenceRequise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompetenceRequise))
            )
            .andExpect(status().isOk());

        // Validate the CompetenceRequise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompetenceRequiseUpdatableFieldsEquals(
            partialUpdatedCompetenceRequise,
            getPersistedCompetenceRequise(partialUpdatedCompetenceRequise)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCompetenceRequise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        competenceRequise.setId(longCount.incrementAndGet());

        // Create the CompetenceRequise
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetenceRequiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, competenceRequiseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(competenceRequiseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompetenceRequise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        competenceRequise.setId(longCount.incrementAndGet());

        // Create the CompetenceRequise
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceRequiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(competenceRequiseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompetenceRequise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        competenceRequise.setId(longCount.incrementAndGet());

        // Create the CompetenceRequise
        CompetenceRequiseDTO competenceRequiseDTO = competenceRequiseMapper.toDto(competenceRequise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetenceRequiseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(competenceRequiseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompetenceRequise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompetenceRequise() throws Exception {
        // Initialize the database
        insertedCompetenceRequise = competenceRequiseRepository.saveAndFlush(competenceRequise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the competenceRequise
        restCompetenceRequiseMockMvc
            .perform(delete(ENTITY_API_URL_ID, competenceRequise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return competenceRequiseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CompetenceRequise getPersistedCompetenceRequise(CompetenceRequise competenceRequise) {
        return competenceRequiseRepository.findById(competenceRequise.getId()).orElseThrow();
    }

    protected void assertPersistedCompetenceRequiseToMatchAllProperties(CompetenceRequise expectedCompetenceRequise) {
        assertCompetenceRequiseAllPropertiesEquals(expectedCompetenceRequise, getPersistedCompetenceRequise(expectedCompetenceRequise));
    }

    protected void assertPersistedCompetenceRequiseToMatchUpdatableProperties(CompetenceRequise expectedCompetenceRequise) {
        assertCompetenceRequiseAllUpdatablePropertiesEquals(
            expectedCompetenceRequise,
            getPersistedCompetenceRequise(expectedCompetenceRequise)
        );
    }
}
