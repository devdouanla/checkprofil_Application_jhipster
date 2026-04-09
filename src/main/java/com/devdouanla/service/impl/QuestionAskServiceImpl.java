package com.devdouanla.service.impl;

import com.devdouanla.domain.QuestionAsk;
import com.devdouanla.repository.QuestionAskRepository;
import com.devdouanla.service.QuestionAskService;
import com.devdouanla.service.dto.QuestionAskDTO;
import com.devdouanla.service.mapper.QuestionAskMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.devdouanla.domain.QuestionAsk}.
 */
@Service
@Transactional
public class QuestionAskServiceImpl implements QuestionAskService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionAskServiceImpl.class);

    private final QuestionAskRepository questionAskRepository;

    private final QuestionAskMapper questionAskMapper;

    public QuestionAskServiceImpl(QuestionAskRepository questionAskRepository, QuestionAskMapper questionAskMapper) {
        this.questionAskRepository = questionAskRepository;
        this.questionAskMapper = questionAskMapper;
    }

    @Override
    public QuestionAskDTO save(QuestionAskDTO questionAskDTO) {
        LOG.debug("Request to save QuestionAsk : {}", questionAskDTO);
        QuestionAsk questionAsk = questionAskMapper.toEntity(questionAskDTO);
        questionAsk = questionAskRepository.save(questionAsk);
        return questionAskMapper.toDto(questionAsk);
    }

    @Override
    public QuestionAskDTO update(QuestionAskDTO questionAskDTO) {
        LOG.debug("Request to update QuestionAsk : {}", questionAskDTO);
        QuestionAsk questionAsk = questionAskMapper.toEntity(questionAskDTO);
        questionAsk = questionAskRepository.save(questionAsk);
        return questionAskMapper.toDto(questionAsk);
    }

    @Override
    public Optional<QuestionAskDTO> partialUpdate(QuestionAskDTO questionAskDTO) {
        LOG.debug("Request to partially update QuestionAsk : {}", questionAskDTO);

        return questionAskRepository
            .findById(questionAskDTO.getId())
            .map(existingQuestionAsk -> {
                questionAskMapper.partialUpdate(existingQuestionAsk, questionAskDTO);

                return existingQuestionAsk;
            })
            .map(questionAskRepository::save)
            .map(questionAskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionAskDTO> findAll() {
        LOG.debug("Request to get all QuestionAsks");
        return questionAskRepository.findAll().stream().map(questionAskMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionAskDTO> findOne(Long id) {
        LOG.debug("Request to get QuestionAsk : {}", id);
        return questionAskRepository.findById(id).map(questionAskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete QuestionAsk : {}", id);
        questionAskRepository.deleteById(id);
    }
}
