package com.devdouanla.web.rest;

import static com.devdouanla.domain.ManagerAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.Manager;
import com.devdouanla.domain.User;
import com.devdouanla.repository.ManagerRepository;
import com.devdouanla.repository.UserRepository;
import com.devdouanla.service.ManagerService;
import com.devdouanla.service.dto.ManagerDTO;
import com.devdouanla.service.mapper.ManagerMapper;
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
 * Integration tests for the {@link ManagerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ManagerResourceIT {

    private static final String ENTITY_API_URL = "/api/managers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ManagerRepository managerRepositoryMock;

    @Autowired
    private ManagerMapper managerMapper;

    @Mock
    private ManagerService managerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManagerMockMvc;

    private Manager manager;

    private Manager insertedManager;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manager createEntity(EntityManager em) {
        Manager manager = new Manager();
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        manager.setUser(user);
        return manager;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manager createUpdatedEntity(EntityManager em) {
        Manager updatedManager = new Manager();
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedManager.setUser(user);
        return updatedManager;
    }

    @BeforeEach
    void initTest() {
        manager = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedManager != null) {
            managerRepository.delete(insertedManager);
            insertedManager = null;
        }
    }

    @Test
    @Transactional
    void createManager() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);
        var returnedManagerDTO = om.readValue(
            restManagerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(managerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ManagerDTO.class
        );

        // Validate the Manager in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedManager = managerMapper.toEntity(returnedManagerDTO);
        assertManagerUpdatableFieldsEquals(returnedManager, getPersistedManager(returnedManager));

        insertedManager = returnedManager;
    }

    @Test
    @Transactional
    void createManagerWithExistingId() throws Exception {
        // Create the Manager with an existing ID
        manager.setId(1L);
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManagerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(managerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllManagers() throws Exception {
        // Initialize the database
        insertedManager = managerRepository.saveAndFlush(manager);

        // Get all the managerList
        restManagerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manager.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllManagersWithEagerRelationshipsIsEnabled() throws Exception {
        when(managerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restManagerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(managerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllManagersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(managerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restManagerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(managerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getManager() throws Exception {
        // Initialize the database
        insertedManager = managerRepository.saveAndFlush(manager);

        // Get the manager
        restManagerMockMvc
            .perform(get(ENTITY_API_URL_ID, manager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manager.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingManager() throws Exception {
        // Get the manager
        restManagerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManager() throws Exception {
        // Initialize the database
        insertedManager = managerRepository.saveAndFlush(manager);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manager
        Manager updatedManager = managerRepository.findById(manager.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedManager are not directly saved in db
        em.detach(updatedManager);
        ManagerDTO managerDTO = managerMapper.toDto(updatedManager);

        restManagerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, managerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(managerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedManagerToMatchAllProperties(updatedManager);
    }

    @Test
    @Transactional
    void putNonExistingManager() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manager.setId(longCount.incrementAndGet());

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, managerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(managerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManager() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manager.setId(longCount.incrementAndGet());

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(managerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManager() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manager.setId(longCount.incrementAndGet());

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(managerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManagerWithPatch() throws Exception {
        // Initialize the database
        insertedManager = managerRepository.saveAndFlush(manager);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manager using partial update
        Manager partialUpdatedManager = new Manager();
        partialUpdatedManager.setId(manager.getId());

        restManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManager.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManager))
            )
            .andExpect(status().isOk());

        // Validate the Manager in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManagerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedManager, manager), getPersistedManager(manager));
    }

    @Test
    @Transactional
    void fullUpdateManagerWithPatch() throws Exception {
        // Initialize the database
        insertedManager = managerRepository.saveAndFlush(manager);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manager using partial update
        Manager partialUpdatedManager = new Manager();
        partialUpdatedManager.setId(manager.getId());

        restManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManager.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManager))
            )
            .andExpect(status().isOk());

        // Validate the Manager in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManagerUpdatableFieldsEquals(partialUpdatedManager, getPersistedManager(partialUpdatedManager));
    }

    @Test
    @Transactional
    void patchNonExistingManager() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manager.setId(longCount.incrementAndGet());

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, managerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(managerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManager() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manager.setId(longCount.incrementAndGet());

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(managerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManager() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manager.setId(longCount.incrementAndGet());

        // Create the Manager
        ManagerDTO managerDTO = managerMapper.toDto(manager);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(managerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manager in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManager() throws Exception {
        // Initialize the database
        insertedManager = managerRepository.saveAndFlush(manager);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the manager
        restManagerMockMvc
            .perform(delete(ENTITY_API_URL_ID, manager.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return managerRepository.count();
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

    protected Manager getPersistedManager(Manager manager) {
        return managerRepository.findById(manager.getId()).orElseThrow();
    }

    protected void assertPersistedManagerToMatchAllProperties(Manager expectedManager) {
        assertManagerAllPropertiesEquals(expectedManager, getPersistedManager(expectedManager));
    }

    protected void assertPersistedManagerToMatchUpdatableProperties(Manager expectedManager) {
        assertManagerAllUpdatablePropertiesEquals(expectedManager, getPersistedManager(expectedManager));
    }
}
