package com.devdouanla.web.rest;

import static com.devdouanla.domain.QuestionAskAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.QuestionAsk;
import com.devdouanla.repository.QuestionAskRepository;
import com.devdouanla.service.dto.QuestionAskDTO;
import com.devdouanla.service.mapper.QuestionAskMapper;
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
 * Integration tests for the {@link QuestionAskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionAskResourceIT {

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final String ENTITY_API_URL = "/api/question-asks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionAskRepository questionAskRepository;

    @Autowired
    private QuestionAskMapper questionAskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionAskMockMvc;

    private QuestionAsk questionAsk;

    private QuestionAsk insertedQuestionAsk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAsk createEntity() {
        return new QuestionAsk().ordre(DEFAULT_ORDRE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAsk createUpdatedEntity() {
        return new QuestionAsk().ordre(UPDATED_ORDRE);
    }

    @BeforeEach
    void initTest() {
        questionAsk = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestionAsk != null) {
            questionAskRepository.delete(insertedQuestionAsk);
            insertedQuestionAsk = null;
        }
    }

    @Test
    @Transactional
    void createQuestionAsk() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuestionAsk
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);
        var returnedQuestionAskDTO = om.readValue(
            restQuestionAskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionAskDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionAskDTO.class
        );

        // Validate the QuestionAsk in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestionAsk = questionAskMapper.toEntity(returnedQuestionAskDTO);
        assertQuestionAskUpdatableFieldsEquals(returnedQuestionAsk, getPersistedQuestionAsk(returnedQuestionAsk));

        insertedQuestionAsk = returnedQuestionAsk;
    }

    @Test
    @Transactional
    void createQuestionAskWithExistingId() throws Exception {
        // Create the QuestionAsk with an existing ID
        questionAsk.setId(1L);
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionAskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionAskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionAsks() throws Exception {
        // Initialize the database
        insertedQuestionAsk = questionAskRepository.saveAndFlush(questionAsk);

        // Get all the questionAskList
        restQuestionAskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionAsk.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)));
    }

    @Test
    @Transactional
    void getQuestionAsk() throws Exception {
        // Initialize the database
        insertedQuestionAsk = questionAskRepository.saveAndFlush(questionAsk);

        // Get the questionAsk
        restQuestionAskMockMvc
            .perform(get(ENTITY_API_URL_ID, questionAsk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionAsk.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE));
    }

    @Test
    @Transactional
    void getNonExistingQuestionAsk() throws Exception {
        // Get the questionAsk
        restQuestionAskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionAsk() throws Exception {
        // Initialize the database
        insertedQuestionAsk = questionAskRepository.saveAndFlush(questionAsk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionAsk
        QuestionAsk updatedQuestionAsk = questionAskRepository.findById(questionAsk.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestionAsk are not directly saved in db
        em.detach(updatedQuestionAsk);
        updatedQuestionAsk.ordre(UPDATED_ORDRE);
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(updatedQuestionAsk);

        restQuestionAskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionAskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionAskDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionAskToMatchAllProperties(updatedQuestionAsk);
    }

    @Test
    @Transactional
    void putNonExistingQuestionAsk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionAsk.setId(longCount.incrementAndGet());

        // Create the QuestionAsk
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionAskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionAskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionAskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionAsk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionAsk.setId(longCount.incrementAndGet());

        // Create the QuestionAsk
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionAskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionAsk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionAsk.setId(longCount.incrementAndGet());

        // Create the QuestionAsk
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionAskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionAskWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionAsk = questionAskRepository.saveAndFlush(questionAsk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionAsk using partial update
        QuestionAsk partialUpdatedQuestionAsk = new QuestionAsk();
        partialUpdatedQuestionAsk.setId(questionAsk.getId());

        restQuestionAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionAsk.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionAsk))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAsk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionAskUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuestionAsk, questionAsk),
            getPersistedQuestionAsk(questionAsk)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuestionAskWithPatch() throws Exception {
        // Initialize the database
        insertedQuestionAsk = questionAskRepository.saveAndFlush(questionAsk);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the questionAsk using partial update
        QuestionAsk partialUpdatedQuestionAsk = new QuestionAsk();
        partialUpdatedQuestionAsk.setId(questionAsk.getId());

        partialUpdatedQuestionAsk.ordre(UPDATED_ORDRE);

        restQuestionAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionAsk.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestionAsk))
            )
            .andExpect(status().isOk());

        // Validate the QuestionAsk in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionAskUpdatableFieldsEquals(partialUpdatedQuestionAsk, getPersistedQuestionAsk(partialUpdatedQuestionAsk));
    }

    @Test
    @Transactional
    void patchNonExistingQuestionAsk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionAsk.setId(longCount.incrementAndGet());

        // Create the QuestionAsk
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionAskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionAskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionAsk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionAsk.setId(longCount.incrementAndGet());

        // Create the QuestionAsk
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionAskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionAsk() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        questionAsk.setId(longCount.incrementAndGet());

        // Create the QuestionAsk
        QuestionAskDTO questionAskDTO = questionAskMapper.toDto(questionAsk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionAskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionAskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionAsk in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionAsk() throws Exception {
        // Initialize the database
        insertedQuestionAsk = questionAskRepository.saveAndFlush(questionAsk);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the questionAsk
        restQuestionAskMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionAsk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionAskRepository.count();
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

    protected QuestionAsk getPersistedQuestionAsk(QuestionAsk questionAsk) {
        return questionAskRepository.findById(questionAsk.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionAskToMatchAllProperties(QuestionAsk expectedQuestionAsk) {
        assertQuestionAskAllPropertiesEquals(expectedQuestionAsk, getPersistedQuestionAsk(expectedQuestionAsk));
    }

    protected void assertPersistedQuestionAskToMatchUpdatableProperties(QuestionAsk expectedQuestionAsk) {
        assertQuestionAskAllUpdatablePropertiesEquals(expectedQuestionAsk, getPersistedQuestionAsk(expectedQuestionAsk));
    }
}
