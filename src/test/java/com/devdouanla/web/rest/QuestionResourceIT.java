package com.devdouanla.web.rest;

import static com.devdouanla.domain.QuestionAsserts.*;
import static com.devdouanla.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devdouanla.IntegrationTest;
import com.devdouanla.domain.Competence;
import com.devdouanla.domain.Question;
import com.devdouanla.domain.enumeration.Difficulte;
import com.devdouanla.repository.QuestionRepository;
import com.devdouanla.service.dto.QuestionDTO;
import com.devdouanla.service.mapper.QuestionMapper;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    private static final String DEFAULT_ENONCE = "AAAAAAAAAA";
    private static final String UPDATED_ENONCE = "BBBBBBBBBB";

    private static final String DEFAULT_REPONSE_TEXTE = "AAAAAAAAAA";
    private static final String UPDATED_REPONSE_TEXTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final String DEFAULT_EXPLICATION = "AAAAAAAAAA";
    private static final String UPDATED_EXPLICATION = "BBBBBBBBBB";

    private static final Difficulte DEFAULT_DIFFICULTE = Difficulte.FACILE;
    private static final Difficulte UPDATED_DIFFICULTE = Difficulte.MOYEN;

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    private Question insertedQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity() {
        return new Question()
            .enonce(DEFAULT_ENONCE)
            .reponseTexte(DEFAULT_REPONSE_TEXTE)
            .points(DEFAULT_POINTS)
            .explication(DEFAULT_EXPLICATION)
            .difficulte(DEFAULT_DIFFICULTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity() {
        return new Question()
            .enonce(UPDATED_ENONCE)
            .reponseTexte(UPDATED_REPONSE_TEXTE)
            .points(UPDATED_POINTS)
            .explication(UPDATED_EXPLICATION)
            .difficulte(UPDATED_DIFFICULTE);
    }

    @BeforeEach
    void initTest() {
        question = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestion != null) {
            questionRepository.delete(insertedQuestion);
            insertedQuestion = null;
        }
    }

    @Test
    @Transactional
    void createQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        var returnedQuestionDTO = om.readValue(
            restQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionDTO.class
        );

        // Validate the Question in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestion = questionMapper.toEntity(returnedQuestionDTO);
        assertQuestionUpdatableFieldsEquals(returnedQuestion, getPersistedQuestion(returnedQuestion));

        insertedQuestion = returnedQuestion;
    }

    @Test
    @Transactional
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnonceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setEnonce(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReponseTexteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setReponseTexte(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setPoints(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDifficulteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setDifficulte(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].enonce").value(hasItem(DEFAULT_ENONCE)))
            .andExpect(jsonPath("$.[*].reponseTexte").value(hasItem(DEFAULT_REPONSE_TEXTE)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].explication").value(hasItem(DEFAULT_EXPLICATION)))
            .andExpect(jsonPath("$.[*].difficulte").value(hasItem(DEFAULT_DIFFICULTE.toString())));
    }

    @Test
    @Transactional
    void getQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.enonce").value(DEFAULT_ENONCE))
            .andExpect(jsonPath("$.reponseTexte").value(DEFAULT_REPONSE_TEXTE))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.explication").value(DEFAULT_EXPLICATION))
            .andExpect(jsonPath("$.difficulte").value(DEFAULT_DIFFICULTE.toString()));
    }

    @Test
    @Transactional
    void getQuestionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        Long id = question.getId();

        defaultQuestionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuestionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuestionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionsByEnonceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where enonce equals to
        defaultQuestionFiltering("enonce.equals=" + DEFAULT_ENONCE, "enonce.equals=" + UPDATED_ENONCE);
    }

    @Test
    @Transactional
    void getAllQuestionsByEnonceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where enonce in
        defaultQuestionFiltering("enonce.in=" + DEFAULT_ENONCE + "," + UPDATED_ENONCE, "enonce.in=" + UPDATED_ENONCE);
    }

    @Test
    @Transactional
    void getAllQuestionsByEnonceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where enonce is not null
        defaultQuestionFiltering("enonce.specified=true", "enonce.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByEnonceContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where enonce contains
        defaultQuestionFiltering("enonce.contains=" + DEFAULT_ENONCE, "enonce.contains=" + UPDATED_ENONCE);
    }

    @Test
    @Transactional
    void getAllQuestionsByEnonceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where enonce does not contain
        defaultQuestionFiltering("enonce.doesNotContain=" + UPDATED_ENONCE, "enonce.doesNotContain=" + DEFAULT_ENONCE);
    }

    @Test
    @Transactional
    void getAllQuestionsByReponseTexteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where reponseTexte equals to
        defaultQuestionFiltering("reponseTexte.equals=" + DEFAULT_REPONSE_TEXTE, "reponseTexte.equals=" + UPDATED_REPONSE_TEXTE);
    }

    @Test
    @Transactional
    void getAllQuestionsByReponseTexteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where reponseTexte in
        defaultQuestionFiltering(
            "reponseTexte.in=" + DEFAULT_REPONSE_TEXTE + "," + UPDATED_REPONSE_TEXTE,
            "reponseTexte.in=" + UPDATED_REPONSE_TEXTE
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByReponseTexteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where reponseTexte is not null
        defaultQuestionFiltering("reponseTexte.specified=true", "reponseTexte.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByReponseTexteContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where reponseTexte contains
        defaultQuestionFiltering("reponseTexte.contains=" + DEFAULT_REPONSE_TEXTE, "reponseTexte.contains=" + UPDATED_REPONSE_TEXTE);
    }

    @Test
    @Transactional
    void getAllQuestionsByReponseTexteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where reponseTexte does not contain
        defaultQuestionFiltering(
            "reponseTexte.doesNotContain=" + UPDATED_REPONSE_TEXTE,
            "reponseTexte.doesNotContain=" + DEFAULT_REPONSE_TEXTE
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points equals to
        defaultQuestionFiltering("points.equals=" + DEFAULT_POINTS, "points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points in
        defaultQuestionFiltering("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS, "points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is not null
        defaultQuestionFiltering("points.specified=true", "points.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is greater than or equal to
        defaultQuestionFiltering("points.greaterThanOrEqual=" + DEFAULT_POINTS, "points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is less than or equal to
        defaultQuestionFiltering("points.lessThanOrEqual=" + DEFAULT_POINTS, "points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is less than
        defaultQuestionFiltering("points.lessThan=" + UPDATED_POINTS, "points.lessThan=" + DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where points is greater than
        defaultQuestionFiltering("points.greaterThan=" + SMALLER_POINTS, "points.greaterThan=" + DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void getAllQuestionsByExplicationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where explication equals to
        defaultQuestionFiltering("explication.equals=" + DEFAULT_EXPLICATION, "explication.equals=" + UPDATED_EXPLICATION);
    }

    @Test
    @Transactional
    void getAllQuestionsByExplicationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where explication in
        defaultQuestionFiltering(
            "explication.in=" + DEFAULT_EXPLICATION + "," + UPDATED_EXPLICATION,
            "explication.in=" + UPDATED_EXPLICATION
        );
    }

    @Test
    @Transactional
    void getAllQuestionsByExplicationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where explication is not null
        defaultQuestionFiltering("explication.specified=true", "explication.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByExplicationContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where explication contains
        defaultQuestionFiltering("explication.contains=" + DEFAULT_EXPLICATION, "explication.contains=" + UPDATED_EXPLICATION);
    }

    @Test
    @Transactional
    void getAllQuestionsByExplicationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where explication does not contain
        defaultQuestionFiltering("explication.doesNotContain=" + UPDATED_EXPLICATION, "explication.doesNotContain=" + DEFAULT_EXPLICATION);
    }

    @Test
    @Transactional
    void getAllQuestionsByDifficulteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where difficulte equals to
        defaultQuestionFiltering("difficulte.equals=" + DEFAULT_DIFFICULTE, "difficulte.equals=" + UPDATED_DIFFICULTE);
    }

    @Test
    @Transactional
    void getAllQuestionsByDifficulteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where difficulte in
        defaultQuestionFiltering("difficulte.in=" + DEFAULT_DIFFICULTE + "," + UPDATED_DIFFICULTE, "difficulte.in=" + UPDATED_DIFFICULTE);
    }

    @Test
    @Transactional
    void getAllQuestionsByDifficulteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where difficulte is not null
        defaultQuestionFiltering("difficulte.specified=true", "difficulte.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCompetenceIsEqualToSomething() throws Exception {
        Competence competence;
        if (TestUtil.findAll(em, Competence.class).isEmpty()) {
            questionRepository.saveAndFlush(question);
            competence = CompetenceResourceIT.createEntity();
        } else {
            competence = TestUtil.findAll(em, Competence.class).get(0);
        }
        em.persist(competence);
        em.flush();
        question.setCompetence(competence);
        questionRepository.saveAndFlush(question);
        Long competenceId = competence.getId();
        // Get all the questionList where competence equals to competenceId
        defaultQuestionShouldBeFound("competenceId.equals=" + competenceId);

        // Get all the questionList where competence equals to (competenceId + 1)
        defaultQuestionShouldNotBeFound("competenceId.equals=" + (competenceId + 1));
    }

    private void defaultQuestionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuestionShouldBeFound(shouldBeFound);
        defaultQuestionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].enonce").value(hasItem(DEFAULT_ENONCE)))
            .andExpect(jsonPath("$.[*].reponseTexte").value(hasItem(DEFAULT_REPONSE_TEXTE)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].explication").value(hasItem(DEFAULT_EXPLICATION)))
            .andExpect(jsonPath("$.[*].difficulte").value(hasItem(DEFAULT_DIFFICULTE.toString())));

        // Check, that the count call also returns 1
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .enonce(UPDATED_ENONCE)
            .reponseTexte(UPDATED_REPONSE_TEXTE)
            .points(UPDATED_POINTS)
            .explication(UPDATED_EXPLICATION)
            .difficulte(UPDATED_DIFFICULTE);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionToMatchAllProperties(updatedQuestion);
    }

    @Test
    @Transactional
    void putNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .enonce(UPDATED_ENONCE)
            .reponseTexte(UPDATED_REPONSE_TEXTE)
            .explication(UPDATED_EXPLICATION)
            .difficulte(UPDATED_DIFFICULTE);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuestion, question), getPersistedQuestion(question));
    }

    @Test
    @Transactional
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .enonce(UPDATED_ENONCE)
            .reponseTexte(UPDATED_REPONSE_TEXTE)
            .points(UPDATED_POINTS)
            .explication(UPDATED_EXPLICATION)
            .difficulte(UPDATED_DIFFICULTE);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(partialUpdatedQuestion, getPersistedQuestion(partialUpdatedQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the question
        restQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, question.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionRepository.count();
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

    protected Question getPersistedQuestion(Question question) {
        return questionRepository.findById(question.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionToMatchAllProperties(Question expectedQuestion) {
        assertQuestionAllPropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }

    protected void assertPersistedQuestionToMatchUpdatableProperties(Question expectedQuestion) {
        assertQuestionAllUpdatablePropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }
}
