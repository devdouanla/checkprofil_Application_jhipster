package com.devdouanla.service.mapper;

import com.devdouanla.domain.Question;
import com.devdouanla.domain.ReponseCandidat;
import com.devdouanla.domain.SessionTest;
import com.devdouanla.service.dto.QuestionDTO;
import com.devdouanla.service.dto.ReponseCandidatDTO;
import com.devdouanla.service.dto.SessionTestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReponseCandidat} and its DTO {@link ReponseCandidatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReponseCandidatMapper extends EntityMapper<ReponseCandidatDTO, ReponseCandidat> {
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    @Mapping(target = "session", source = "session", qualifiedByName = "sessionTestId")
    ReponseCandidatDTO toDto(ReponseCandidat s);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);

    @Named("sessionTestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SessionTestDTO toDtoSessionTestId(SessionTest sessionTest);
}
