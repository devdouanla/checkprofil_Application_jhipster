package com.devdouanla.web.rest;

import static com.devdouanla.domain.ResultatAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.Resultat;
import com.devdouanla.domain.enumeration.Mention;
import com.devdouanla.repository.ResultatRepository;
import com.devdouanla.service.dto.ResultatDTO;
import com.devdouanla.service.mapper.ResultatMapper;
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
 * Integration tests for the {@link ResultatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResultatResourceIT {

    private static final Float DEFAULT_SCORE_TOTAL = 1F;
    private static final Float UPDATED_SCORE_TOTAL = 2F;

    private static final Float DEFAULT_SCORE_MAX = 1F;
    private static final Float UPDATED_SCORE_MAX = 2F;

    private static final Float DEFAULT_POURCENTAGE = 1F;
    private static final Float UPDATED_POURCENTAGE = 2F;

    private static final Mention DEFAULT_MENTION = Mention.EXCELLENT;
    private static final Mention UPDATED_MENTION = Mention.BIEN;

    private static final String ENTITY_API_URL = "/api/resultats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResultatRepository resultatRepository;

    @Autowired
    private ResultatMapper resultatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultatMockMvc;

    private Resultat resultat;

    private Resultat insertedResultat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultat createEntity() {
        return new Resultat()
            .scoreTotal(DEFAULT_SCORE_TOTAL)
            .scoreMax(DEFAULT_SCORE_MAX)
            .pourcentage(DEFAULT_POURCENTAGE)
            .mention(DEFAULT_MENTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resultat createUpdatedEntity() {
        return new Resultat()
            .scoreTotal(UPDATED_SCORE_TOTAL)
            .scoreMax(UPDATED_SCORE_MAX)
            .pourcentage(UPDATED_POURCENTAGE)
            .mention(UPDATED_MENTION);
    }

    @BeforeEach
    void initTest() {
        resultat = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedResultat != null) {
            resultatRepository.delete(insertedResultat);
            insertedResultat = null;
        }
    }

    @Test
    @Transactional
    void createResultat() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);
        var returnedResultatDTO = om.readValue(
            restResultatMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResultatDTO.class
        );

        // Validate the Resultat in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResultat = resultatMapper.toEntity(returnedResultatDTO);
        assertResultatUpdatableFieldsEquals(returnedResultat, getPersistedResultat(returnedResultat));

        insertedResultat = returnedResultat;
    }

    @Test
    @Transactional
    void createResultatWithExistingId() throws Exception {
        // Create the Resultat with an existing ID
        resultat.setId(1L);
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScoreTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resultat.setScoreTotal(null);

        // Create the Resultat, which fails.
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        restResultatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScoreMaxIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resultat.setScoreMax(null);

        // Create the Resultat, which fails.
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        restResultatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPourcentageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resultat.setPourcentage(null);

        // Create the Resultat, which fails.
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        restResultatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMentionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resultat.setMention(null);

        // Create the Resultat, which fails.
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        restResultatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResultats() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        // Get all the resultatList
        restResultatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultat.getId().intValue())))
            .andExpect(jsonPath("$.[*].scoreTotal").value(hasItem(DEFAULT_SCORE_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].scoreMax").value(hasItem(DEFAULT_SCORE_MAX.doubleValue())))
            .andExpect(jsonPath("$.[*].pourcentage").value(hasItem(DEFAULT_POURCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].mention").value(hasItem(DEFAULT_MENTION.toString())));
    }

    @Test
    @Transactional
    void getResultat() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        // Get the resultat
        restResultatMockMvc
            .perform(get(ENTITY_API_URL_ID, resultat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultat.getId().intValue()))
            .andExpect(jsonPath("$.scoreTotal").value(DEFAULT_SCORE_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.scoreMax").value(DEFAULT_SCORE_MAX.doubleValue()))
            .andExpect(jsonPath("$.pourcentage").value(DEFAULT_POURCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.mention").value(DEFAULT_MENTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingResultat() throws Exception {
        // Get the resultat
        restResultatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResultat() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resultat
        Resultat updatedResultat = resultatRepository.findById(resultat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResultat are not directly saved in db
        em.detach(updatedResultat);
        updatedResultat
            .scoreTotal(UPDATED_SCORE_TOTAL)
            .scoreMax(UPDATED_SCORE_MAX)
            .pourcentage(UPDATED_POURCENTAGE)
            .mention(UPDATED_MENTION);
        ResultatDTO resultatDTO = resultatMapper.toDto(updatedResultat);

        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResultatToMatchAllProperties(updatedResultat);
    }

    @Test
    @Transactional
    void putNonExistingResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResultatWithPatch() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resultat using partial update
        Resultat partialUpdatedResultat = new Resultat();
        partialUpdatedResultat.setId(resultat.getId());

        partialUpdatedResultat.scoreMax(UPDATED_SCORE_MAX);

        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResultat))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResultatUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedResultat, resultat), getPersistedResultat(resultat));
    }

    @Test
    @Transactional
    void fullUpdateResultatWithPatch() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resultat using partial update
        Resultat partialUpdatedResultat = new Resultat();
        partialUpdatedResultat.setId(resultat.getId());

        partialUpdatedResultat
            .scoreTotal(UPDATED_SCORE_TOTAL)
            .scoreMax(UPDATED_SCORE_MAX)
            .pourcentage(UPDATED_POURCENTAGE)
            .mention(UPDATED_MENTION);

        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultat.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResultat))
            )
            .andExpect(status().isOk());

        // Validate the Resultat in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResultatUpdatableFieldsEquals(partialUpdatedResultat, getPersistedResultat(partialUpdatedResultat));
    }

    @Test
    @Transactional
    void patchNonExistingResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resultatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultat() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resultat.setId(longCount.incrementAndGet());

        // Create the Resultat
        ResultatDTO resultatDTO = resultatMapper.toDto(resultat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resultatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resultat in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResultat() throws Exception {
        // Initialize the database
        insertedResultat = resultatRepository.saveAndFlush(resultat);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resultat
        restResultatMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resultatRepository.count();
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

    protected Resultat getPersistedResultat(Resultat resultat) {
        return resultatRepository.findById(resultat.getId()).orElseThrow();
    }

    protected void assertPersistedResultatToMatchAllProperties(Resultat expectedResultat) {
        assertResultatAllPropertiesEquals(expectedResultat, getPersistedResultat(expectedResultat));
    }

    protected void assertPersistedResultatToMatchUpdatableProperties(Resultat expectedResultat) {
        assertResultatAllUpdatablePropertiesEquals(expectedResultat, getPersistedResultat(expectedResultat));
    }
}
