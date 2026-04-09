package com.devdouanla.service.impl;

import com.devdouanla.domain.enumeration.Difficulte;
import com.devdouanla.service.EpreuveService;
import com.devdouanla.service.EvaluationService;
import com.devdouanla.service.QuestionAskService;
import com.devdouanla.service.QuestionQueryService;
import com.devdouanla.service.criteria.QuestionCriteria;
import com.devdouanla.service.dto.EpreuveDTO;
import com.devdouanla.service.dto.EvaluationDTO;
import com.devdouanla.service.dto.QuestionAskDTO;
import com.devdouanla.service.dto.QuestionDTO;
import com.devdouanla.service.QuestionAskService;
import com.devdouanla.service.dto.SessionTestDTO;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import com.devdouanla.domain.SessionTest;
import com.devdouanla.repository.SessionTestRepository;
import com.devdouanla.service.SessionTestService;
import com.devdouanla.service.dto.SessionTestDTO;
import com.devdouanla.service.mapper.SessionTestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devdouanla.domain.SessionTest}.
 */
@Service
@Transactional
public class SessionTestServiceImpl implements SessionTestService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTestServiceImpl.class);

    private final SessionTestRepository sessionTestRepository;

    private final SessionTestMapper sessionTestMapper;

    private final EpreuveService epreuveService;

    private final EvaluationService evaluationService;

    private final QuestionQueryService questionQueryService;

    private final QuestionAskService questionAskService;

    public SessionTestServiceImpl(
        SessionTestRepository sessionTestRepository,
        SessionTestMapper sessionTestMapper,
        EpreuveService epreuveService,
        EvaluationService evaluationService,
        QuestionQueryService questionQueryService,
        QuestionAskService questionAskService
    ) {
        this.sessionTestRepository = sessionTestRepository;
        this.sessionTestMapper = sessionTestMapper;
        this.epreuveService = epreuveService;
        this.evaluationService = evaluationService;
        this.questionQueryService = questionQueryService;
        this.questionAskService = questionAskService;
    }

    @Override
    public SessionTestDTO save(SessionTestDTO sessionTestDTO) {
        LOG.debug("Request to save SessionTest : {}", sessionTestDTO);
        SessionTest sessionTest = sessionTestMapper.toEntity(sessionTestDTO);
        sessionTest = sessionTestRepository.save(sessionTest);
        return sessionTestMapper.toDto(sessionTest);
    }

    @Override
    public SessionTestDTO update(SessionTestDTO sessionTestDTO) {
        LOG.debug("Request to update SessionTest : {}", sessionTestDTO);
        SessionTest sessionTest = sessionTestMapper.toEntity(sessionTestDTO);
        sessionTest = sessionTestRepository.save(sessionTest);
        return sessionTestMapper.toDto(sessionTest);
    }

    @Override
    public Optional<SessionTestDTO> partialUpdate(SessionTestDTO sessionTestDTO) {
        LOG.debug("Request to partially update SessionTest : {}", sessionTestDTO);

        return sessionTestRepository
            .findById(sessionTestDTO.getId())
            .map(existingSessionTest -> {
                sessionTestMapper.partialUpdate(existingSessionTest, sessionTestDTO);

                return existingSessionTest;
            })
            .map(sessionTestRepository::save)
            .map(sessionTestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionTestDTO> findOne(Long id) {
        LOG.debug("Request to get SessionTest : {}", id);
        return sessionTestRepository.findById(id).map(sessionTestMapper::toDto);
    }

@Override
    public void delete(Long id) {
        LOG.debug("Request to delete SessionTest : {}", id);
        sessionTestRepository.deleteById(id);
    }

    @Override
    @Transactional
    public SessionTestDTO generateSessionTest(Long evaluationId, Long epreuveId) {
        LOG.debug("Generate SessionTest for evaluation {} and epreuve {}", evaluationId, epreuveId);

        // Fetch epreuve
        EpreuveDTO epreuveDTO = epreuveService.findOne(epreuveId)
            .orElseThrow(() -> new RuntimeException("Epreuve not found: " + epreuveId));
        
        LOG.debug("Epreuve: titre={}, competence.id={}, difficulte={}, nbQuestions={}", 
                  epreuveDTO.getTitre(), epreuveDTO.getCompetence().getId(), epreuveDTO.getDifficulte(), epreuveDTO.getNbQuestions());
        
        Long competenceId = epreuveDTO.getCompetence().getId();
        Difficulte difficulte = epreuveDTO.getDifficulte();
        Integer nbQuestions = epreuveDTO.getNbQuestions();

        // Fetch evaluation
        EvaluationDTO evaluationDTO = evaluationService.findOne(evaluationId)
            .orElseThrow(() -> new RuntimeException("Evaluation not found: " + evaluationId));

        // Filter questions by criteria
        QuestionCriteria criteria = new QuestionCriteria();
        criteria.competenceId().setEquals(competenceId);
        criteria.difficulte().setEquals(difficulte);

        Pageable pageable = PageRequest.of(0, 1000, Sort.unsorted());
        Page<QuestionDTO> questionPage = questionQueryService.findByCriteria(criteria, pageable);

        List<QuestionDTO> candidates = questionPage.getContent();
        if (candidates.isEmpty()) {
            LOG.warn("No questions available for competence {} and difficulty {}. SessionTest created empty.", competenceId, difficulte);
            // Continue with empty session or throw - here continue
        }

        // Random shuffle and select
        Collections.shuffle(candidates, new SecureRandom());
        int toSelect = candidates.isEmpty() ? 0 : Math.min(nbQuestions, candidates.size());
        List<QuestionDTO> selectedQuestions = candidates.isEmpty() ? List.of() : candidates.subList(0, toSelect);

        // Create SessionTest
        SessionTestDTO sessionTestDTO = new SessionTestDTO();
        sessionTestDTO.setEvaluation(evaluationDTO);
        sessionTestDTO.setEpreuve(epreuveDTO);
        sessionTestDTO.setDateDebut(Instant.now());
        sessionTestDTO = save(sessionTestDTO);

        // Create QuestionAsks
        for (int i = 0; i < selectedQuestions.size(); i++) {
            QuestionDTO questionDTO = selectedQuestions.get(i);
            QuestionAskDTO qaDTO = new QuestionAskDTO();
            qaDTO.setQuestion(questionDTO);
            qaDTO.setSession(sessionTestDTO);
            qaDTO.setOrdre(i + 1);
            questionAskService.save(qaDTO);
        }

        // Reload to include questionsAsks
        return findOne(sessionTestDTO.getId()).orElseThrow();
    }
}

