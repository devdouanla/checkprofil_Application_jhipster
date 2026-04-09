package com.devdouanla.web.rest;

import static com.devdouanla.domain.EpreuveAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.Competence;
import com.devdouanla.domain.Epreuve;
import com.devdouanla.domain.enumeration.Difficulte;
import com.devdouanla.repository.EpreuveRepository;
import com.devdouanla.service.dto.EpreuveDTO;
import com.devdouanla.service.mapper.EpreuveMapper;
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
 * Integration tests for the {@link EpreuveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EpreuveResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_ENONCE = "AAAAAAAAAA";
    private static final String UPDATED_ENONCE = "BBBBBBBBBB";

    private static final Difficulte DEFAULT_DIFFICULTE = Difficulte.FACILE;
    private static final Difficulte UPDATED_DIFFICULTE = Difficulte.MOYEN;

    private static final Integer DEFAULT_DUREE = 1;
    private static final Integer UPDATED_DUREE = 2;
    private static final Integer SMALLER_DUREE = 1 - 1;

    private static final Integer DEFAULT_NB_QUESTIONS = 1;
    private static final Integer UPDATED_NB_QUESTIONS = 2;
    private static final Integer SMALLER_NB_QUESTIONS = 1 - 1;

    private static final Boolean DEFAULT_GENERE_PAR_IA = false;
    private static final Boolean UPDATED_GENERE_PAR_IA = true;

    private static final Boolean DEFAULT_PUBLIE = false;
    private static final Boolean UPDATED_PUBLIE = true;

    private static final String ENTITY_API_URL = "/api/epreuves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EpreuveRepository epreuveRepository;

    @Autowired
    private EpreuveMapper epreuveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEpreuveMockMvc;

    private Epreuve epreuve;

    private Epreuve insertedEpreuve;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epreuve createEntity(EntityManager em) {
        Epreuve epreuve = new Epreuve()
            .titre(DEFAULT_TITRE)
            .enonce(DEFAULT_ENONCE)
            .difficulte(DEFAULT_DIFFICULTE)
            .duree(DEFAULT_DUREE)
            .nbQuestions(DEFAULT_NB_QUESTIONS)
            .genereParIA(DEFAULT_GENERE_PAR_IA)
            .publie(DEFAULT_PUBLIE);
        // Add required entity
        Competence competence;
        if (TestUtil.findAll(em, Competence.class).isEmpty()) {
            competence = CompetenceResourceIT.createEntity();
            em.persist(competence);
            em.flush();
        } else {
            competence = TestUtil.findAll(em, Competence.class).get(0);
        }
        epreuve.setCompetence(competence);
        return epreuve;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Epreuve createUpdatedEntity(EntityManager em) {
        Epreuve updatedEpreuve = new Epreuve()
            .titre(UPDATED_TITRE)
            .enonce(UPDATED_ENONCE)
            .difficulte(UPDATED_DIFFICULTE)
            .duree(UPDATED_DUREE)
            .nbQuestions(UPDATED_NB_QUESTIONS)
            .genereParIA(UPDATED_GENERE_PAR_IA)
            .publie(UPDATED_PUBLIE);
        // Add required entity
        Competence competence;
        if (TestUtil.findAll(em, Competence.class).isEmpty()) {
            competence = CompetenceResourceIT.createUpdatedEntity();
            em.persist(competence);
            em.flush();
        } else {
            competence = TestUtil.findAll(em, Competence.class).get(0);
        }
        updatedEpreuve.setCompetence(competence);
        return updatedEpreuve;
    }

    @BeforeEach
    void initTest() {
        epreuve = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEpreuve != null) {
            epreuveRepository.delete(insertedEpreuve);
            insertedEpreuve = null;
        }
    }

    @Test
    @Transactional
    void createEpreuve() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Epreuve
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);
        var returnedEpreuveDTO = om.readValue(
            restEpreuveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EpreuveDTO.class
        );

        // Validate the Epreuve in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEpreuve = epreuveMapper.toEntity(returnedEpreuveDTO);
        assertEpreuveUpdatableFieldsEquals(returnedEpreuve, getPersistedEpreuve(returnedEpreuve));

        insertedEpreuve = returnedEpreuve;
    }

    @Test
    @Transactional
    void createEpreuveWithExistingId() throws Exception {
        // Create the Epreuve with an existing ID
        epreuve.setId(1L);
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        epreuve.setTitre(null);

        // Create the Epreuve, which fails.
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnonceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        epreuve.setEnonce(null);

        // Create the Epreuve, which fails.
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDifficulteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        epreuve.setDifficulte(null);

        // Create the Epreuve, which fails.
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDureeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        epreuve.setDuree(null);

        // Create the Epreuve, which fails.
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbQuestionsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        epreuve.setNbQuestions(null);

        // Create the Epreuve, which fails.
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenereParIAIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        epreuve.setGenereParIA(null);

        // Create the Epreuve, which fails.
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPublieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        epreuve.setPublie(null);

        // Create the Epreuve, which fails.
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        restEpreuveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEpreuves() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList
        restEpreuveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epreuve.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].enonce").value(hasItem(DEFAULT_ENONCE)))
            .andExpect(jsonPath("$.[*].difficulte").value(hasItem(DEFAULT_DIFFICULTE.toString())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].nbQuestions").value(hasItem(DEFAULT_NB_QUESTIONS)))
            .andExpect(jsonPath("$.[*].genereParIA").value(hasItem(DEFAULT_GENERE_PAR_IA)))
            .andExpect(jsonPath("$.[*].publie").value(hasItem(DEFAULT_PUBLIE)));
    }

    @Test
    @Transactional
    void getEpreuve() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get the epreuve
        restEpreuveMockMvc
            .perform(get(ENTITY_API_URL_ID, epreuve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(epreuve.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.enonce").value(DEFAULT_ENONCE))
            .andExpect(jsonPath("$.difficulte").value(DEFAULT_DIFFICULTE.toString()))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE))
            .andExpect(jsonPath("$.nbQuestions").value(DEFAULT_NB_QUESTIONS))
            .andExpect(jsonPath("$.genereParIA").value(DEFAULT_GENERE_PAR_IA))
            .andExpect(jsonPath("$.publie").value(DEFAULT_PUBLIE));
    }

    @Test
    @Transactional
    void getEpreuvesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        Long id = epreuve.getId();

        defaultEpreuveFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEpreuveFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEpreuveFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEpreuvesByTitreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where titre equals to
        defaultEpreuveFiltering("titre.equals=" + DEFAULT_TITRE, "titre.equals=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByTitreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where titre in
        defaultEpreuveFiltering("titre.in=" + DEFAULT_TITRE + "," + UPDATED_TITRE, "titre.in=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByTitreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where titre is not null
        defaultEpreuveFiltering("titre.specified=true", "titre.specified=false");
    }

    @Test
    @Transactional
    void getAllEpreuvesByTitreContainsSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where titre contains
        defaultEpreuveFiltering("titre.contains=" + DEFAULT_TITRE, "titre.contains=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByTitreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where titre does not contain
        defaultEpreuveFiltering("titre.doesNotContain=" + UPDATED_TITRE, "titre.doesNotContain=" + DEFAULT_TITRE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByEnonceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where enonce equals to
        defaultEpreuveFiltering("enonce.equals=" + DEFAULT_ENONCE, "enonce.equals=" + UPDATED_ENONCE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByEnonceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where enonce in
        defaultEpreuveFiltering("enonce.in=" + DEFAULT_ENONCE + "," + UPDATED_ENONCE, "enonce.in=" + UPDATED_ENONCE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByEnonceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where enonce is not null
        defaultEpreuveFiltering("enonce.specified=true", "enonce.specified=false");
    }

    @Test
    @Transactional
    void getAllEpreuvesByEnonceContainsSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where enonce contains
        defaultEpreuveFiltering("enonce.contains=" + DEFAULT_ENONCE, "enonce.contains=" + UPDATED_ENONCE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByEnonceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where enonce does not contain
        defaultEpreuveFiltering("enonce.doesNotContain=" + UPDATED_ENONCE, "enonce.doesNotContain=" + DEFAULT_ENONCE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDifficulteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where difficulte equals to
        defaultEpreuveFiltering("difficulte.equals=" + DEFAULT_DIFFICULTE, "difficulte.equals=" + UPDATED_DIFFICULTE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDifficulteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where difficulte in
        defaultEpreuveFiltering("difficulte.in=" + DEFAULT_DIFFICULTE + "," + UPDATED_DIFFICULTE, "difficulte.in=" + UPDATED_DIFFICULTE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDifficulteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where difficulte is not null
        defaultEpreuveFiltering("difficulte.specified=true", "difficulte.specified=false");
    }

    @Test
    @Transactional
    void getAllEpreuvesByDureeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where duree equals to
        defaultEpreuveFiltering("duree.equals=" + DEFAULT_DUREE, "duree.equals=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDureeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where duree in
        defaultEpreuveFiltering("duree.in=" + DEFAULT_DUREE + "," + UPDATED_DUREE, "duree.in=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDureeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where duree is not null
        defaultEpreuveFiltering("duree.specified=true", "duree.specified=false");
    }

    @Test
    @Transactional
    void getAllEpreuvesByDureeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where duree is greater than or equal to
        defaultEpreuveFiltering("duree.greaterThanOrEqual=" + DEFAULT_DUREE, "duree.greaterThanOrEqual=" + UPDATED_DUREE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDureeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where duree is less than or equal to
        defaultEpreuveFiltering("duree.lessThanOrEqual=" + DEFAULT_DUREE, "duree.lessThanOrEqual=" + SMALLER_DUREE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDureeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where duree is less than
        defaultEpreuveFiltering("duree.lessThan=" + UPDATED_DUREE, "duree.lessThan=" + DEFAULT_DUREE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByDureeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where duree is greater than
        defaultEpreuveFiltering("duree.greaterThan=" + SMALLER_DUREE, "duree.greaterThan=" + DEFAULT_DUREE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByNbQuestionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where nbQuestions equals to
        defaultEpreuveFiltering("nbQuestions.equals=" + DEFAULT_NB_QUESTIONS, "nbQuestions.equals=" + UPDATED_NB_QUESTIONS);
    }

    @Test
    @Transactional
    void getAllEpreuvesByNbQuestionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where nbQuestions in
        defaultEpreuveFiltering(
            "nbQuestions.in=" + DEFAULT_NB_QUESTIONS + "," + UPDATED_NB_QUESTIONS,
            "nbQuestions.in=" + UPDATED_NB_QUESTIONS
        );
    }

    @Test
    @Transactional
    void getAllEpreuvesByNbQuestionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where nbQuestions is not null
        defaultEpreuveFiltering("nbQuestions.specified=true", "nbQuestions.specified=false");
    }

    @Test
    @Transactional
    void getAllEpreuvesByNbQuestionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where nbQuestions is greater than or equal to
        defaultEpreuveFiltering(
            "nbQuestions.greaterThanOrEqual=" + DEFAULT_NB_QUESTIONS,
            "nbQuestions.greaterThanOrEqual=" + UPDATED_NB_QUESTIONS
        );
    }

    @Test
    @Transactional
    void getAllEpreuvesByNbQuestionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where nbQuestions is less than or equal to
        defaultEpreuveFiltering(
            "nbQuestions.lessThanOrEqual=" + DEFAULT_NB_QUESTIONS,
            "nbQuestions.lessThanOrEqual=" + SMALLER_NB_QUESTIONS
        );
    }

    @Test
    @Transactional
    void getAllEpreuvesByNbQuestionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where nbQuestions is less than
        defaultEpreuveFiltering("nbQuestions.lessThan=" + UPDATED_NB_QUESTIONS, "nbQuestions.lessThan=" + DEFAULT_NB_QUESTIONS);
    }

    @Test
    @Transactional
    void getAllEpreuvesByNbQuestionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where nbQuestions is greater than
        defaultEpreuveFiltering("nbQuestions.greaterThan=" + SMALLER_NB_QUESTIONS, "nbQuestions.greaterThan=" + DEFAULT_NB_QUESTIONS);
    }

    @Test
    @Transactional
    void getAllEpreuvesByGenereParIAIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where genereParIA equals to
        defaultEpreuveFiltering("genereParIA.equals=" + DEFAULT_GENERE_PAR_IA, "genereParIA.equals=" + UPDATED_GENERE_PAR_IA);
    }

    @Test
    @Transactional
    void getAllEpreuvesByGenereParIAIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where genereParIA in
        defaultEpreuveFiltering(
            "genereParIA.in=" + DEFAULT_GENERE_PAR_IA + "," + UPDATED_GENERE_PAR_IA,
            "genereParIA.in=" + UPDATED_GENERE_PAR_IA
        );
    }

    @Test
    @Transactional
    void getAllEpreuvesByGenereParIAIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where genereParIA is not null
        defaultEpreuveFiltering("genereParIA.specified=true", "genereParIA.specified=false");
    }

    @Test
    @Transactional
    void getAllEpreuvesByPublieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where publie equals to
        defaultEpreuveFiltering("publie.equals=" + DEFAULT_PUBLIE, "publie.equals=" + UPDATED_PUBLIE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByPublieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where publie in
        defaultEpreuveFiltering("publie.in=" + DEFAULT_PUBLIE + "," + UPDATED_PUBLIE, "publie.in=" + UPDATED_PUBLIE);
    }

    @Test
    @Transactional
    void getAllEpreuvesByPublieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        // Get all the epreuveList where publie is not null
        defaultEpreuveFiltering("publie.specified=true", "publie.specified=false");
    }

    @Test
    @Transactional
    void getAllEpreuvesByCompetenceIsEqualToSomething() throws Exception {
        Competence competence;
        if (TestUtil.findAll(em, Competence.class).isEmpty()) {
            epreuveRepository.saveAndFlush(epreuve);
            competence = CompetenceResourceIT.createEntity();
        } else {
            competence = TestUtil.findAll(em, Competence.class).get(0);
        }
        em.persist(competence);
        em.flush();
        epreuve.setCompetence(competence);
        epreuveRepository.saveAndFlush(epreuve);
        Long competenceId = competence.getId();
        // Get all the epreuveList where competence equals to competenceId
        defaultEpreuveShouldBeFound("competenceId.equals=" + competenceId);

        // Get all the epreuveList where competence equals to (competenceId + 1)
        defaultEpreuveShouldNotBeFound("competenceId.equals=" + (competenceId + 1));
    }

    private void defaultEpreuveFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEpreuveShouldBeFound(shouldBeFound);
        defaultEpreuveShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEpreuveShouldBeFound(String filter) throws Exception {
        restEpreuveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(epreuve.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].enonce").value(hasItem(DEFAULT_ENONCE)))
            .andExpect(jsonPath("$.[*].difficulte").value(hasItem(DEFAULT_DIFFICULTE.toString())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE)))
            .andExpect(jsonPath("$.[*].nbQuestions").value(hasItem(DEFAULT_NB_QUESTIONS)))
            .andExpect(jsonPath("$.[*].genereParIA").value(hasItem(DEFAULT_GENERE_PAR_IA)))
            .andExpect(jsonPath("$.[*].publie").value(hasItem(DEFAULT_PUBLIE)));

        // Check, that the count call also returns 1
        restEpreuveMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEpreuveShouldNotBeFound(String filter) throws Exception {
        restEpreuveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEpreuveMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEpreuve() throws Exception {
        // Get the epreuve
        restEpreuveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEpreuve() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the epreuve
        Epreuve updatedEpreuve = epreuveRepository.findById(epreuve.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEpreuve are not directly saved in db
        em.detach(updatedEpreuve);
        updatedEpreuve
            .titre(UPDATED_TITRE)
            .enonce(UPDATED_ENONCE)
            .difficulte(UPDATED_DIFFICULTE)
            .duree(UPDATED_DUREE)
            .nbQuestions(UPDATED_NB_QUESTIONS)
            .genereParIA(UPDATED_GENERE_PAR_IA)
            .publie(UPDATED_PUBLIE);
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(updatedEpreuve);

        restEpreuveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, epreuveDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO))
            )
            .andExpect(status().isOk());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEpreuveToMatchAllProperties(updatedEpreuve);
    }

    @Test
    @Transactional
    void putNonExistingEpreuve() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        epreuve.setId(longCount.incrementAndGet());

        // Create the Epreuve
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpreuveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, epreuveDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEpreuve() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        epreuve.setId(longCount.incrementAndGet());

        // Create the Epreuve
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpreuveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(epreuveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEpreuve() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        epreuve.setId(longCount.incrementAndGet());

        // Create the Epreuve
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpreuveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEpreuveWithPatch() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the epreuve using partial update
        Epreuve partialUpdatedEpreuve = new Epreuve();
        partialUpdatedEpreuve.setId(epreuve.getId());

        partialUpdatedEpreuve
            .titre(UPDATED_TITRE)
            .enonce(UPDATED_ENONCE)
            .difficulte(UPDATED_DIFFICULTE)
            .nbQuestions(UPDATED_NB_QUESTIONS)
            .genereParIA(UPDATED_GENERE_PAR_IA);

        restEpreuveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEpreuve.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEpreuve))
            )
            .andExpect(status().isOk());

        // Validate the Epreuve in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEpreuveUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEpreuve, epreuve), getPersistedEpreuve(epreuve));
    }

    @Test
    @Transactional
    void fullUpdateEpreuveWithPatch() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the epreuve using partial update
        Epreuve partialUpdatedEpreuve = new Epreuve();
        partialUpdatedEpreuve.setId(epreuve.getId());

        partialUpdatedEpreuve
            .titre(UPDATED_TITRE)
            .enonce(UPDATED_ENONCE)
            .difficulte(UPDATED_DIFFICULTE)
            .duree(UPDATED_DUREE)
            .nbQuestions(UPDATED_NB_QUESTIONS)
            .genereParIA(UPDATED_GENERE_PAR_IA)
            .publie(UPDATED_PUBLIE);

        restEpreuveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEpreuve.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEpreuve))
            )
            .andExpect(status().isOk());

        // Validate the Epreuve in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEpreuveUpdatableFieldsEquals(partialUpdatedEpreuve, getPersistedEpreuve(partialUpdatedEpreuve));
    }

    @Test
    @Transactional
    void patchNonExistingEpreuve() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        epreuve.setId(longCount.incrementAndGet());

        // Create the Epreuve
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEpreuveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, epreuveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(epreuveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEpreuve() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        epreuve.setId(longCount.incrementAndGet());

        // Create the Epreuve
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpreuveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(epreuveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEpreuve() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        epreuve.setId(longCount.incrementAndGet());

        // Create the Epreuve
        EpreuveDTO epreuveDTO = epreuveMapper.toDto(epreuve);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEpreuveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(epreuveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Epreuve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEpreuve() throws Exception {
        // Initialize the database
        insertedEpreuve = epreuveRepository.saveAndFlush(epreuve);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the epreuve
        restEpreuveMockMvc
            .perform(delete(ENTITY_API_URL_ID, epreuve.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return epreuveRepository.count();
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

    protected Epreuve getPersistedEpreuve(Epreuve epreuve) {
        return epreuveRepository.findById(epreuve.getId()).orElseThrow();
    }

    protected void assertPersistedEpreuveToMatchAllProperties(Epreuve expectedEpreuve) {
        assertEpreuveAllPropertiesEquals(expectedEpreuve, getPersistedEpreuve(expectedEpreuve));
    }

    protected void assertPersistedEpreuveToMatchUpdatableProperties(Epreuve expectedEpreuve) {
        assertEpreuveAllUpdatablePropertiesEquals(expectedEpreuve, getPersistedEpreuve(expectedEpreuve));
    }
}
