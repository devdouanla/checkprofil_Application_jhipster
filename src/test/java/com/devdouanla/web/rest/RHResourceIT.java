package com.devdouanla.web.rest;

import static com.devdouanla.domain.RHAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.RH;
import com.devdouanla.domain.User;
import com.devdouanla.repository.RHRepository;
import com.devdouanla.repository.UserRepository;
import com.devdouanla.service.RHService;
import com.devdouanla.service.dto.RHDTO;
import com.devdouanla.service.mapper.RHMapper;
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
 * Integration tests for the {@link RHResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RHResourceIT {

    private static final String ENTITY_API_URL = "/api/rhs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RHRepository rHRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private RHRepository rHRepositoryMock;

    @Autowired
    private RHMapper rHMapper;

    @Mock
    private RHService rHServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRHMockMvc;

    private RH rH;

    private RH insertedRH;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RH createEntity(EntityManager em) {
        RH rH = new RH();
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        rH.setUser(user);
        return rH;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RH createUpdatedEntity(EntityManager em) {
        RH updatedRH = new RH();
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedRH.setUser(user);
        return updatedRH;
    }

    @BeforeEach
    void initTest() {
        rH = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRH != null) {
            rHRepository.delete(insertedRH);
            insertedRH = null;
        }
    }

    @Test
    @Transactional
    void createRH() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RH
        RHDTO rHDTO = rHMapper.toDto(rH);
        var returnedRHDTO = om.readValue(
            restRHMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rHDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RHDTO.class
        );

        // Validate the RH in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRH = rHMapper.toEntity(returnedRHDTO);
        assertRHUpdatableFieldsEquals(returnedRH, getPersistedRH(returnedRH));

        insertedRH = returnedRH;
    }

    @Test
    @Transactional
    void createRHWithExistingId() throws Exception {
        // Create the RH with an existing ID
        rH.setId(1L);
        RHDTO rHDTO = rHMapper.toDto(rH);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRHMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rHDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRHS() throws Exception {
        // Initialize the database
        insertedRH = rHRepository.saveAndFlush(rH);

        // Get all the rHList
        restRHMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rH.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRHSWithEagerRelationshipsIsEnabled() throws Exception {
        when(rHServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRHMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rHServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRHSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rHServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRHMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(rHRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRH() throws Exception {
        // Initialize the database
        insertedRH = rHRepository.saveAndFlush(rH);

        // Get the rH
        restRHMockMvc
            .perform(get(ENTITY_API_URL_ID, rH.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rH.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRH() throws Exception {
        // Get the rH
        restRHMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRH() throws Exception {
        // Initialize the database
        insertedRH = rHRepository.saveAndFlush(rH);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rH
        RH updatedRH = rHRepository.findById(rH.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRH are not directly saved in db
        em.detach(updatedRH);
        RHDTO rHDTO = rHMapper.toDto(updatedRH);

        restRHMockMvc
            .perform(put(ENTITY_API_URL_ID, rHDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rHDTO)))
            .andExpect(status().isOk());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRHToMatchAllProperties(updatedRH);
    }

    @Test
    @Transactional
    void putNonExistingRH() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rH.setId(longCount.incrementAndGet());

        // Create the RH
        RHDTO rHDTO = rHMapper.toDto(rH);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRHMockMvc
            .perform(put(ENTITY_API_URL_ID, rHDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rHDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRH() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rH.setId(longCount.incrementAndGet());

        // Create the RH
        RHDTO rHDTO = rHMapper.toDto(rH);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRHMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rHDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRH() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rH.setId(longCount.incrementAndGet());

        // Create the RH
        RHDTO rHDTO = rHMapper.toDto(rH);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRHMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rHDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRHWithPatch() throws Exception {
        // Initialize the database
        insertedRH = rHRepository.saveAndFlush(rH);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rH using partial update
        RH partialUpdatedRH = new RH();
        partialUpdatedRH.setId(rH.getId());

        restRHMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRH.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRH))
            )
            .andExpect(status().isOk());

        // Validate the RH in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRHUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRH, rH), getPersistedRH(rH));
    }

    @Test
    @Transactional
    void fullUpdateRHWithPatch() throws Exception {
        // Initialize the database
        insertedRH = rHRepository.saveAndFlush(rH);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rH using partial update
        RH partialUpdatedRH = new RH();
        partialUpdatedRH.setId(rH.getId());

        restRHMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRH.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRH))
            )
            .andExpect(status().isOk());

        // Validate the RH in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRHUpdatableFieldsEquals(partialUpdatedRH, getPersistedRH(partialUpdatedRH));
    }

    @Test
    @Transactional
    void patchNonExistingRH() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rH.setId(longCount.incrementAndGet());

        // Create the RH
        RHDTO rHDTO = rHMapper.toDto(rH);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRHMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rHDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rHDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRH() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rH.setId(longCount.incrementAndGet());

        // Create the RH
        RHDTO rHDTO = rHMapper.toDto(rH);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRHMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rHDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRH() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rH.setId(longCount.incrementAndGet());

        // Create the RH
        RHDTO rHDTO = rHMapper.toDto(rH);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRHMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rHDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RH in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRH() throws Exception {
        // Initialize the database
        insertedRH = rHRepository.saveAndFlush(rH);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rH
        restRHMockMvc.perform(delete(ENTITY_API_URL_ID, rH.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rHRepository.count();
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

    protected RH getPersistedRH(RH rH) {
        return rHRepository.findById(rH.getId()).orElseThrow();
    }

    protected void assertPersistedRHToMatchAllProperties(RH expectedRH) {
        assertRHAllPropertiesEquals(expectedRH, getPersistedRH(expectedRH));
    }

    protected void assertPersistedRHToMatchUpdatableProperties(RH expectedRH) {
        assertRHAllUpdatablePropertiesEquals(expectedRH, getPersistedRH(expectedRH));
    }
}
