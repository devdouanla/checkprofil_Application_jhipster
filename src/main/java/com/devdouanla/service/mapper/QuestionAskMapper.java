package com.devdouanla.service.mapper;

import com.devdouanla.domain.Question;
import com.devdouanla.domain.QuestionAsk;
import com.devdouanla.domain.SessionTest;
import com.devdouanla.service.dto.QuestionAskDTO;
import com.devdouanla.service.dto.QuestionDTO;
import com.devdouanla.service.dto.SessionTestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuestionAsk} and its DTO {@link QuestionAskDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionAskMapper extends EntityMapper<QuestionAskDTO, QuestionAsk> {
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    @Mapping(target = "session", source = "session", qualifiedByName = "sessionTestId")
    QuestionAskDTO toDto(QuestionAsk s);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);

    @Named("sessionTestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SessionTestDTO toDtoSessionTestId(SessionTest sessionTest);
}
