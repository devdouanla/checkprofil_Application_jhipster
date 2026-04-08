package com.devdouanla.web.rest;

import static com.devdouanla.domain.SessionTestAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.Epreuve;
import com.devdouanla.domain.Evaluation;
import com.devdouanla.domain.Resultat;
import com.devdouanla.domain.SessionTest;
import com.devdouanla.repository.SessionTestRepository;
import com.devdouanla.service.dto.SessionTestDTO;
import com.devdouanla.service.mapper.SessionTestMapper;
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
 * Integration tests for the {@link SessionTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionTestResourceIT {

    private static final Float DEFAULT_SCORE_OBTENU = 1F;
    private static final Float UPDATED_SCORE_OBTENU = 2F;
    private static final Float SMALLER_SCORE_OBTENU = 1F - 1F;

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/session-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SessionTestRepository sessionTestRepository;

    @Autowired
    private SessionTestMapper sessionTestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionTestMockMvc;

    private SessionTest sessionTest;

    private SessionTest insertedSessionTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionTest createEntity() {
        return new SessionTest().scoreObtenu(DEFAULT_SCORE_OBTENU).dateDebut(DEFAULT_DATE_DEBUT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionTest createUpdatedEntity() {
        return new SessionTest().scoreObtenu(UPDATED_SCORE_OBTENU).dateDebut(UPDATED_DATE_DEBUT);
    }

    @BeforeEach
    void initTest() {
        sessionTest = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSessionTest != null) {
            sessionTestRepository.delete(insertedSessionTest);
            insertedSessionTest = null;
        }
    }

    @Test
    @Transactional
    void createSessionTest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SessionTest
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);
        var returnedSessionTestDTO = om.readValue(
            restSessionTestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SessionTestDTO.class
        );

        // Validate the SessionTest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSessionTest = sessionTestMapper.toEntity(returnedSessionTestDTO);
        assertSessionTestUpdatableFieldsEquals(returnedSessionTest, getPersistedSessionTest(returnedSessionTest));

        insertedSessionTest = returnedSessionTest;
    }

    @Test
    @Transactional
    void createSessionTestWithExistingId() throws Exception {
        // Create the SessionTest with an existing ID
        sessionTest.setId(1L);
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sessionTest.setDateDebut(null);

        // Create the SessionTest, which fails.
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        restSessionTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSessionTests() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList
        restSessionTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].scoreObtenu").value(hasItem(DEFAULT_SCORE_OBTENU.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())));
    }

    @Test
    @Transactional
    void getSessionTest() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get the sessionTest
        restSessionTestMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionTest.getId().intValue()))
            .andExpect(jsonPath("$.scoreObtenu").value(DEFAULT_SCORE_OBTENU.doubleValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()));
    }

    @Test
    @Transactional
    void getSessionTestsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        Long id = sessionTest.getId();

        defaultSessionTestFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSessionTestFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSessionTestFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSessionTestsByScoreObtenuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where scoreObtenu equals to
        defaultSessionTestFiltering("scoreObtenu.equals=" + DEFAULT_SCORE_OBTENU, "scoreObtenu.equals=" + UPDATED_SCORE_OBTENU);
    }

    @Test
    @Transactional
    void getAllSessionTestsByScoreObtenuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where scoreObtenu in
        defaultSessionTestFiltering(
            "scoreObtenu.in=" + DEFAULT_SCORE_OBTENU + "," + UPDATED_SCORE_OBTENU,
            "scoreObtenu.in=" + UPDATED_SCORE_OBTENU
        );
    }

    @Test
    @Transactional
    void getAllSessionTestsByScoreObtenuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where scoreObtenu is not null
        defaultSessionTestFiltering("scoreObtenu.specified=true", "scoreObtenu.specified=false");
    }

    @Test
    @Transactional
    void getAllSessionTestsByScoreObtenuIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where scoreObtenu is greater than or equal to
        defaultSessionTestFiltering(
            "scoreObtenu.greaterThanOrEqual=" + DEFAULT_SCORE_OBTENU,
            "scoreObtenu.greaterThanOrEqual=" + UPDATED_SCORE_OBTENU
        );
    }

    @Test
    @Transactional
    void getAllSessionTestsByScoreObtenuIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where scoreObtenu is less than or equal to
        defaultSessionTestFiltering(
            "scoreObtenu.lessThanOrEqual=" + DEFAULT_SCORE_OBTENU,
            "scoreObtenu.lessThanOrEqual=" + SMALLER_SCORE_OBTENU
        );
    }

    @Test
    @Transactional
    void getAllSessionTestsByScoreObtenuIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where scoreObtenu is less than
        defaultSessionTestFiltering("scoreObtenu.lessThan=" + UPDATED_SCORE_OBTENU, "scoreObtenu.lessThan=" + DEFAULT_SCORE_OBTENU);
    }

    @Test
    @Transactional
    void getAllSessionTestsByScoreObtenuIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where scoreObtenu is greater than
        defaultSessionTestFiltering("scoreObtenu.greaterThan=" + SMALLER_SCORE_OBTENU, "scoreObtenu.greaterThan=" + DEFAULT_SCORE_OBTENU);
    }

    @Test
    @Transactional
    void getAllSessionTestsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where dateDebut equals to
        defaultSessionTestFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTestsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where dateDebut in
        defaultSessionTestFiltering("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT, "dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTestsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        // Get all the sessionTestList where dateDebut is not null
        defaultSessionTestFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllSessionTestsByResultatIsEqualToSomething() throws Exception {
        Resultat resultat;
        if (TestUtil.findAll(em, Resultat.class).isEmpty()) {
            sessionTestRepository.saveAndFlush(sessionTest);
            resultat = ResultatResourceIT.createEntity();
        } else {
            resultat = TestUtil.findAll(em, Resultat.class).get(0);
        }
        em.persist(resultat);
        em.flush();
        sessionTest.setResultat(resultat);
        sessionTestRepository.saveAndFlush(sessionTest);
        Long resultatId = resultat.getId();
        // Get all the sessionTestList where resultat equals to resultatId
        defaultSessionTestShouldBeFound("resultatId.equals=" + resultatId);

        // Get all the sessionTestList where resultat equals to (resultatId + 1)
        defaultSessionTestShouldNotBeFound("resultatId.equals=" + (resultatId + 1));
    }

    @Test
    @Transactional
    void getAllSessionTestsByEvaluationIsEqualToSomething() throws Exception {
        Evaluation evaluation;
        if (TestUtil.findAll(em, Evaluation.class).isEmpty()) {
            sessionTestRepository.saveAndFlush(sessionTest);
            evaluation = EvaluationResourceIT.createEntity();
        } else {
            evaluation = TestUtil.findAll(em, Evaluation.class).get(0);
        }
        em.persist(evaluation);
        em.flush();
        sessionTest.setEvaluation(evaluation);
        sessionTestRepository.saveAndFlush(sessionTest);
        Long evaluationId = evaluation.getId();
        // Get all the sessionTestList where evaluation equals to evaluationId
        defaultSessionTestShouldBeFound("evaluationId.equals=" + evaluationId);

        // Get all the sessionTestList where evaluation equals to (evaluationId + 1)
        defaultSessionTestShouldNotBeFound("evaluationId.equals=" + (evaluationId + 1));
    }

    @Test
    @Transactional
    void getAllSessionTestsByEpreuvesIsEqualToSomething() throws Exception {
        Epreuve epreuves;
        if (TestUtil.findAll(em, Epreuve.class).isEmpty()) {
            sessionTestRepository.saveAndFlush(sessionTest);
            epreuves = EpreuveResourceIT.createEntity();
        } else {
            epreuves = TestUtil.findAll(em, Epreuve.class).get(0);
        }
        em.persist(epreuves);
        em.flush();
        sessionTest.setEpreuves(epreuves);
        sessionTestRepository.saveAndFlush(sessionTest);
        Long epreuvesId = epreuves.getId();
        // Get all the sessionTestList where epreuves equals to epreuvesId
        defaultSessionTestShouldBeFound("epreuvesId.equals=" + epreuvesId);

        // Get all the sessionTestList where epreuves equals to (epreuvesId + 1)
        defaultSessionTestShouldNotBeFound("epreuvesId.equals=" + (epreuvesId + 1));
    }

    private void defaultSessionTestFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSessionTestShouldBeFound(shouldBeFound);
        defaultSessionTestShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSessionTestShouldBeFound(String filter) throws Exception {
        restSessionTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].scoreObtenu").value(hasItem(DEFAULT_SCORE_OBTENU.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())));

        // Check, that the count call also returns 1
        restSessionTestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSessionTestShouldNotBeFound(String filter) throws Exception {
        restSessionTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSessionTestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSessionTest() throws Exception {
        // Get the sessionTest
        restSessionTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSessionTest() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionTest
        SessionTest updatedSessionTest = sessionTestRepository.findById(sessionTest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSessionTest are not directly saved in db
        em.detach(updatedSessionTest);
        updatedSessionTest.scoreObtenu(UPDATED_SCORE_OBTENU).dateDebut(UPDATED_DATE_DEBUT);
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(updatedSessionTest);

        restSessionTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionTestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionTestDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSessionTestToMatchAllProperties(updatedSessionTest);
    }

    @Test
    @Transactional
    void putNonExistingSessionTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionTest.setId(longCount.incrementAndGet());

        // Create the SessionTest
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionTestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionTest.setId(longCount.incrementAndGet());

        // Create the SessionTest
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sessionTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionTest.setId(longCount.incrementAndGet());

        // Create the SessionTest
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sessionTestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionTestWithPatch() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionTest using partial update
        SessionTest partialUpdatedSessionTest = new SessionTest();
        partialUpdatedSessionTest.setId(sessionTest.getId());

        partialUpdatedSessionTest.scoreObtenu(UPDATED_SCORE_OBTENU).dateDebut(UPDATED_DATE_DEBUT);

        restSessionTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSessionTest))
            )
            .andExpect(status().isOk());

        // Validate the SessionTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionTestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSessionTest, sessionTest),
            getPersistedSessionTest(sessionTest)
        );
    }

    @Test
    @Transactional
    void fullUpdateSessionTestWithPatch() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sessionTest using partial update
        SessionTest partialUpdatedSessionTest = new SessionTest();
        partialUpdatedSessionTest.setId(sessionTest.getId());

        partialUpdatedSessionTest.scoreObtenu(UPDATED_SCORE_OBTENU).dateDebut(UPDATED_DATE_DEBUT);

        restSessionTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSessionTest))
            )
            .andExpect(status().isOk());

        // Validate the SessionTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSessionTestUpdatableFieldsEquals(partialUpdatedSessionTest, getPersistedSessionTest(partialUpdatedSessionTest));
    }

    @Test
    @Transactional
    void patchNonExistingSessionTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionTest.setId(longCount.incrementAndGet());

        // Create the SessionTest
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionTestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sessionTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionTest.setId(longCount.incrementAndGet());

        // Create the SessionTest
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sessionTestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sessionTest.setId(longCount.incrementAndGet());

        // Create the SessionTest
        SessionTestDTO sessionTestDTO = sessionTestMapper.toDto(sessionTest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sessionTestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionTest() throws Exception {
        // Initialize the database
        insertedSessionTest = sessionTestRepository.saveAndFlush(sessionTest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sessionTest
        restSessionTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sessionTestRepository.count();
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

    protected SessionTest getPersistedSessionTest(SessionTest sessionTest) {
        return sessionTestRepository.findById(sessionTest.getId()).orElseThrow();
    }

    protected void assertPersistedSessionTestToMatchAllProperties(SessionTest expectedSessionTest) {
        assertSessionTestAllPropertiesEquals(expectedSessionTest, getPersistedSessionTest(expectedSessionTest));
    }

    protected void assertPersistedSessionTestToMatchUpdatableProperties(SessionTest expectedSessionTest) {
        assertSessionTestAllUpdatablePropertiesEquals(expectedSessionTest, getPersistedSessionTest(expectedSessionTest));
    }
}
