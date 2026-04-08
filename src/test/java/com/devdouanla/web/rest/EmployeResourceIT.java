package com.devdouanla.web.rest;

import static com.devdouanla.domain.EmployeAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.Employe;
import com.devdouanla.domain.Poste;
import com.devdouanla.repository.EmployeRepository;
import com.devdouanla.service.dto.EmployeDTO;
import com.devdouanla.service.mapper.EmployeMapper;
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
 * Integration tests for the {@link EmployeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_RECRUTEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_RECRUTEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/employes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EmployeMapper employeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeMockMvc;

    private Employe employe;

    private Employe insertedEmploye;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employe createEntity() {
        return new Employe().nom(DEFAULT_NOM).dateRecrutement(DEFAULT_DATE_RECRUTEMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employe createUpdatedEntity() {
        return new Employe().nom(UPDATED_NOM).dateRecrutement(UPDATED_DATE_RECRUTEMENT);
    }

    @BeforeEach
    void initTest() {
        employe = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmploye != null) {
            employeRepository.delete(insertedEmploye);
            insertedEmploye = null;
        }
    }

    @Test
    @Transactional
    void createEmploye() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);
        var returnedEmployeDTO = om.readValue(
            restEmployeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeDTO.class
        );

        // Validate the Employe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmploye = employeMapper.toEntity(returnedEmployeDTO);
        assertEmployeUpdatableFieldsEquals(returnedEmploye, getPersistedEmploye(returnedEmploye));

        insertedEmploye = returnedEmploye;
    }

    @Test
    @Transactional
    void createEmployeWithExistingId() throws Exception {
        // Create the Employe with an existing ID
        employe.setId(1L);
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employe.setNom(null);

        // Create the Employe, which fails.
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        restEmployeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateRecrutementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employe.setDateRecrutement(null);

        // Create the Employe, which fails.
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        restEmployeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployes() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateRecrutement").value(hasItem(DEFAULT_DATE_RECRUTEMENT.toString())));
    }

    @Test
    @Transactional
    void getEmploye() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get the employe
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL_ID, employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employe.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.dateRecrutement").value(DEFAULT_DATE_RECRUTEMENT.toString()));
    }

    @Test
    @Transactional
    void getEmployesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        Long id = employe.getId();

        defaultEmployeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where nom equals to
        defaultEmployeFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEmployesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where nom in
        defaultEmployeFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEmployesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where nom is not null
        defaultEmployeFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployesByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where nom contains
        defaultEmployeFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEmployesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where nom does not contain
        defaultEmployeFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllEmployesByDateRecrutementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where dateRecrutement equals to
        defaultEmployeFiltering("dateRecrutement.equals=" + DEFAULT_DATE_RECRUTEMENT, "dateRecrutement.equals=" + UPDATED_DATE_RECRUTEMENT);
    }

    @Test
    @Transactional
    void getAllEmployesByDateRecrutementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where dateRecrutement in
        defaultEmployeFiltering(
            "dateRecrutement.in=" + DEFAULT_DATE_RECRUTEMENT + "," + UPDATED_DATE_RECRUTEMENT,
            "dateRecrutement.in=" + UPDATED_DATE_RECRUTEMENT
        );
    }

    @Test
    @Transactional
    void getAllEmployesByDateRecrutementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        // Get all the employeList where dateRecrutement is not null
        defaultEmployeFiltering("dateRecrutement.specified=true", "dateRecrutement.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployesByPosteIsEqualToSomething() throws Exception {
        Poste poste;
        if (TestUtil.findAll(em, Poste.class).isEmpty()) {
            employeRepository.saveAndFlush(employe);
            poste = PosteResourceIT.createEntity();
        } else {
            poste = TestUtil.findAll(em, Poste.class).get(0);
        }
        em.persist(poste);
        em.flush();
        employe.setPoste(poste);
        employeRepository.saveAndFlush(employe);
        Long posteId = poste.getId();
        // Get all the employeList where poste equals to posteId
        defaultEmployeShouldBeFound("posteId.equals=" + posteId);

        // Get all the employeList where poste equals to (posteId + 1)
        defaultEmployeShouldNotBeFound("posteId.equals=" + (posteId + 1));
    }

    private void defaultEmployeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeShouldBeFound(shouldBeFound);
        defaultEmployeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeShouldBeFound(String filter) throws Exception {
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].dateRecrutement").value(hasItem(DEFAULT_DATE_RECRUTEMENT.toString())));

        // Check, that the count call also returns 1
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeShouldNotBeFound(String filter) throws Exception {
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmploye() throws Exception {
        // Get the employe
        restEmployeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmploye() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employe
        Employe updatedEmploye = employeRepository.findById(employe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmploye are not directly saved in db
        em.detach(updatedEmploye);
        updatedEmploye.nom(UPDATED_NOM).dateRecrutement(UPDATED_DATE_RECRUTEMENT);
        EmployeDTO employeDTO = employeMapper.toDto(updatedEmploye);

        restEmployeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeToMatchAllProperties(updatedEmploye);
    }

    @Test
    @Transactional
    void putNonExistingEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeWithPatch() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employe using partial update
        Employe partialUpdatedEmploye = new Employe();
        partialUpdatedEmploye.setId(employe.getId());

        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploye.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmploye))
            )
            .andExpect(status().isOk());

        // Validate the Employe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmploye, employe), getPersistedEmploye(employe));
    }

    @Test
    @Transactional
    void fullUpdateEmployeWithPatch() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employe using partial update
        Employe partialUpdatedEmploye = new Employe();
        partialUpdatedEmploye.setId(employe.getId());

        partialUpdatedEmploye.nom(UPDATED_NOM).dateRecrutement(UPDATED_DATE_RECRUTEMENT);

        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploye.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmploye))
            )
            .andExpect(status().isOk());

        // Validate the Employe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeUpdatableFieldsEquals(partialUpdatedEmploye, getPersistedEmploye(partialUpdatedEmploye));
    }

    @Test
    @Transactional
    void patchNonExistingEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // Create the Employe
        EmployeDTO employeDTO = employeMapper.toDto(employe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploye() throws Exception {
        // Initialize the database
        insertedEmploye = employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employe
        restEmployeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeRepository.count();
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

    protected Employe getPersistedEmploye(Employe employe) {
        return employeRepository.findById(employe.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeToMatchAllProperties(Employe expectedEmploye) {
        assertEmployeAllPropertiesEquals(expectedEmploye, getPersistedEmploye(expectedEmploye));
    }

    protected void assertPersistedEmployeToMatchUpdatableProperties(Employe expectedEmploye) {
        assertEmployeAllUpdatablePropertiesEquals(expectedEmploye, getPersistedEmploye(expectedEmploye));
    }
}
