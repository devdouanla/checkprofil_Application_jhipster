package com.devdouanla.web.rest;

import static com.devdouanla.domain.ReponseCandidatAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.ReponseCandidat;
import com.devdouanla.repository.ReponseCandidatRepository;
import com.devdouanla.service.dto.ReponseCandidatDTO;
import com.devdouanla.service.mapper.ReponseCandidatMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ReponseCandidatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReponseCandidatResourceIT {

    private static final Boolean DEFAULT_EST_CORRECTE = false;
    private static final Boolean UPDATED_EST_CORRECTE = true;

    private static final Instant DEFAULT_DATE_REPONSE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REPONSE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reponse-candidats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReponseCandidatRepository reponseCandidatRepository;

    @Autowired
    private ReponseCandidatMapper reponseCandidatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReponseCandidatMockMvc;

    private ReponseCandidat reponseCandidat;

    private ReponseCandidat insertedReponseCandidat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReponseCandidat createEntity() {
        return new ReponseCandidat().estCorrecte(DEFAULT_EST_CORRECTE).dateReponse(DEFAULT_DATE_REPONSE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReponseCandidat createUpdatedEntity() {
        return new ReponseCandidat().estCorrecte(UPDATED_EST_CORRECTE).dateReponse(UPDATED_DATE_REPONSE);
    }

    @BeforeEach
    void initTest() {
        reponseCandidat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReponseCandidat != null) {
            reponseCandidatRepository.delete(insertedReponseCandidat);
            insertedReponseCandidat = null;
        }
    }

    @Test
    @Transactional
    void createReponseCandidat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReponseCandidat
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);
        var returnedReponseCandidatDTO = om.readValue(
            restReponseCandidatMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reponseCandidatDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReponseCandidatDTO.class
        );

        // Validate the ReponseCandidat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReponseCandidat = reponseCandidatMapper.toEntity(returnedReponseCandidatDTO);
        assertReponseCandidatUpdatableFieldsEquals(returnedReponseCandidat, getPersistedReponseCandidat(returnedReponseCandidat));

        insertedReponseCandidat = returnedReponseCandidat;
    }

    @Test
    @Transactional
    void createReponseCandidatWithExistingId() throws Exception {
        // Create the ReponseCandidat with an existing ID
        reponseCandidat.setId(1L);
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReponseCandidatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reponseCandidatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEstCorrecteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reponseCandidat.setEstCorrecte(null);

        // Create the ReponseCandidat, which fails.
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        restReponseCandidatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reponseCandidatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateReponseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reponseCandidat.setDateReponse(null);

        // Create the ReponseCandidat, which fails.
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        restReponseCandidatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reponseCandidatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReponseCandidats() throws Exception {
        // Initialize the database
        insertedReponseCandidat = reponseCandidatRepository.saveAndFlush(reponseCandidat);

        // Get all the reponseCandidatList
        restReponseCandidatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reponseCandidat.getId().intValue())))
            .andExpect(jsonPath("$.[*].estCorrecte").value(hasItem(DEFAULT_EST_CORRECTE)))
            .andExpect(jsonPath("$.[*].dateReponse").value(hasItem(DEFAULT_DATE_REPONSE.toString())));
    }

    @Test
    @Transactional
    void getReponseCandidat() throws Exception {
        // Initialize the database
        insertedReponseCandidat = reponseCandidatRepository.saveAndFlush(reponseCandidat);

        // Get the reponseCandidat
        restReponseCandidatMockMvc
            .perform(get(ENTITY_API_URL_ID, reponseCandidat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reponseCandidat.getId().intValue()))
            .andExpect(jsonPath("$.estCorrecte").value(DEFAULT_EST_CORRECTE))
            .andExpect(jsonPath("$.dateReponse").value(DEFAULT_DATE_REPONSE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReponseCandidat() throws Exception {
        // Get the reponseCandidat
        restReponseCandidatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReponseCandidat() throws Exception {
        // Initialize the database
        insertedReponseCandidat = reponseCandidatRepository.saveAndFlush(reponseCandidat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reponseCandidat
        ReponseCandidat updatedReponseCandidat = reponseCandidatRepository.findById(reponseCandidat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReponseCandidat are not directly saved in db
        em.detach(updatedReponseCandidat);
        updatedReponseCandidat.estCorrecte(UPDATED_EST_CORRECTE).dateReponse(UPDATED_DATE_REPONSE);
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(updatedReponseCandidat);

        restReponseCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reponseCandidatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reponseCandidatDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReponseCandidatToMatchAllProperties(updatedReponseCandidat);
    }

    @Test
    @Transactional
    void putNonExistingReponseCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reponseCandidat.setId(longCount.incrementAndGet());

        // Create the ReponseCandidat
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReponseCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reponseCandidatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reponseCandidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReponseCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reponseCandidat.setId(longCount.incrementAndGet());

        // Create the ReponseCandidat
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReponseCandidatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reponseCandidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReponseCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reponseCandidat.setId(longCount.incrementAndGet());

        // Create the ReponseCandidat
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReponseCandidatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reponseCandidatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReponseCandidatWithPatch() throws Exception {
        // Initialize the database
        insertedReponseCandidat = reponseCandidatRepository.saveAndFlush(reponseCandidat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reponseCandidat using partial update
        ReponseCandidat partialUpdatedReponseCandidat = new ReponseCandidat();
        partialUpdatedReponseCandidat.setId(reponseCandidat.getId());

        partialUpdatedReponseCandidat.estCorrecte(UPDATED_EST_CORRECTE).dateReponse(UPDATED_DATE_REPONSE);

        restReponseCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReponseCandidat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReponseCandidat))
            )
            .andExpect(status().isOk());

        // Validate the ReponseCandidat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReponseCandidatUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReponseCandidat, reponseCandidat),
            getPersistedReponseCandidat(reponseCandidat)
        );
    }

    @Test
    @Transactional
    void fullUpdateReponseCandidatWithPatch() throws Exception {
        // Initialize the database
        insertedReponseCandidat = reponseCandidatRepository.saveAndFlush(reponseCandidat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reponseCandidat using partial update
        ReponseCandidat partialUpdatedReponseCandidat = new ReponseCandidat();
        partialUpdatedReponseCandidat.setId(reponseCandidat.getId());

        partialUpdatedReponseCandidat.estCorrecte(UPDATED_EST_CORRECTE).dateReponse(UPDATED_DATE_REPONSE);

        restReponseCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReponseCandidat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReponseCandidat))
            )
            .andExpect(status().isOk());

        // Validate the ReponseCandidat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReponseCandidatUpdatableFieldsEquals(
            partialUpdatedReponseCandidat,
            getPersistedReponseCandidat(partialUpdatedReponseCandidat)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReponseCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reponseCandidat.setId(longCount.incrementAndGet());

        // Create the ReponseCandidat
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReponseCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reponseCandidatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reponseCandidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReponseCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reponseCandidat.setId(longCount.incrementAndGet());

        // Create the ReponseCandidat
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReponseCandidatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reponseCandidatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReponseCandidat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reponseCandidat.setId(longCount.incrementAndGet());

        // Create the ReponseCandidat
        ReponseCandidatDTO reponseCandidatDTO = reponseCandidatMapper.toDto(reponseCandidat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReponseCandidatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reponseCandidatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReponseCandidat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReponseCandidat() throws Exception {
        // Initialize the database
        insertedReponseCandidat = reponseCandidatRepository.saveAndFlush(reponseCandidat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reponseCandidat
        restReponseCandidatMockMvc
            .perform(delete(ENTITY_API_URL_ID, reponseCandidat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reponseCandidatRepository.count();
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

    protected ReponseCandidat getPersistedReponseCandidat(ReponseCandidat reponseCandidat) {
        return reponseCandidatRepository.findById(reponseCandidat.getId()).orElseThrow();
    }

    protected void assertPersistedReponseCandidatToMatchAllProperties(ReponseCandidat expectedReponseCandidat) {
        assertReponseCandidatAllPropertiesEquals(expectedReponseCandidat, getPersistedReponseCandidat(expectedReponseCandidat));
    }

    protected void assertPersistedReponseCandidatToMatchUpdatableProperties(ReponseCandidat expectedReponseCandidat) {
        assertReponseCandidatAllUpdatablePropertiesEquals(expectedReponseCandidat, getPersistedReponseCandidat(expectedReponseCandidat));
    }
}
