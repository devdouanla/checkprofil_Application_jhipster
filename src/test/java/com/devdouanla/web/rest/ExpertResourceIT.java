package com.devdouanla.web.rest;

import static com.devdouanla.domain.ExpertAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.Expert;
import com.devdouanla.domain.User;
import com.devdouanla.repository.ExpertRepository;
import com.devdouanla.repository.UserRepository;
import com.devdouanla.service.ExpertService;
import com.devdouanla.service.dto.ExpertDTO;
import com.devdouanla.service.mapper.ExpertMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExpertResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExpertResourceIT {

    private static final String ENTITY_API_URL = "/api/experts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ExpertRepository expertRepositoryMock;

    @Autowired
    private ExpertMapper expertMapper;

    @Mock
    private ExpertService expertServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpertMockMvc;

    private Expert expert;

    private Expert insertedExpert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expert createEntity(EntityManager em) {
        Expert expert = new Expert();
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        expert.setUser(user);
        return expert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expert createUpdatedEntity(EntityManager em) {
        Expert updatedExpert = new Expert();
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedExpert.setUser(user);
        return updatedExpert;
    }

    @BeforeEach
    void initTest() {
        expert = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExpert != null) {
            expertRepository.delete(insertedExpert);
            insertedExpert = null;
        }
    }

    @Test
    @Transactional
    void createExpert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);
        var returnedExpertDTO = om.readValue(
            restExpertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExpertDTO.class
        );

        // Validate the Expert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExpert = expertMapper.toEntity(returnedExpertDTO);
        assertExpertUpdatableFieldsEquals(returnedExpert, getPersistedExpert(returnedExpert));

        insertedExpert = returnedExpert;
    }

    @Test
    @Transactional
    void createExpertWithExistingId() throws Exception {
        // Create the Expert with an existing ID
        expert.setId(1L);
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExperts() throws Exception {
        // Initialize the database
        insertedExpert = expertRepository.saveAndFlush(expert);

        // Get all the expertList
        restExpertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expert.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExpertsWithEagerRelationshipsIsEnabled() throws Exception {
        when(expertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExpertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(expertServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExpertsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(expertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExpertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(expertRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExpert() throws Exception {
        // Initialize the database
        insertedExpert = expertRepository.saveAndFlush(expert);

        // Get the expert
        restExpertMockMvc
            .perform(get(ENTITY_API_URL_ID, expert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expert.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingExpert() throws Exception {
        // Get the expert
        restExpertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpert() throws Exception {
        // Initialize the database
        insertedExpert = expertRepository.saveAndFlush(expert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expert
        Expert updatedExpert = expertRepository.findById(expert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpert are not directly saved in db
        em.detach(updatedExpert);
        ExpertDTO expertDTO = expertMapper.toDto(updatedExpert);

        restExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expertDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expertDTO))
            )
            .andExpect(status().isOk());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpertToMatchAllProperties(updatedExpert);
    }

    @Test
    @Transactional
    void putNonExistingExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expert.setId(longCount.incrementAndGet());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expertDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expert.setId(longCount.incrementAndGet());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expert.setId(longCount.incrementAndGet());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpertWithPatch() throws Exception {
        // Initialize the database
        insertedExpert = expertRepository.saveAndFlush(expert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expert using partial update
        Expert partialUpdatedExpert = new Expert();
        partialUpdatedExpert.setId(expert.getId());

        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpert))
            )
            .andExpect(status().isOk());

        // Validate the Expert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpertUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedExpert, expert), getPersistedExpert(expert));
    }

    @Test
    @Transactional
    void fullUpdateExpertWithPatch() throws Exception {
        // Initialize the database
        insertedExpert = expertRepository.saveAndFlush(expert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expert using partial update
        Expert partialUpdatedExpert = new Expert();
        partialUpdatedExpert.setId(expert.getId());

        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpert))
            )
            .andExpect(status().isOk());

        // Validate the Expert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpertUpdatableFieldsEquals(partialUpdatedExpert, getPersistedExpert(partialUpdatedExpert));
    }

    @Test
    @Transactional
    void patchNonExistingExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expert.setId(longCount.incrementAndGet());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expert.setId(longCount.incrementAndGet());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expert.setId(longCount.incrementAndGet());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpert() throws Exception {
        // Initialize the database
        insertedExpert = expertRepository.saveAndFlush(expert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the expert
        restExpertMockMvc
            .perform(delete(ENTITY_API_URL_ID, expert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return expertRepository.count();
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

    protected Expert getPersistedExpert(Expert expert) {
        return expertRepository.findById(expert.getId()).orElseThrow();
    }

    protected void assertPersistedExpertToMatchAllProperties(Expert expectedExpert) {
        assertExpertAllPropertiesEquals(expectedExpert, getPersistedExpert(expectedExpert));
    }

    protected void assertPersistedExpertToMatchUpdatableProperties(Expert expectedExpert) {
        assertExpertAllUpdatablePropertiesEquals(expectedExpert, getPersistedExpert(expectedExpert));
    }
}
