package com.devdouanla.service.mapper;

import com.devdouanla.domain.QuestionAsk;
import com.devdouanla.domain.ReponseCandidat;
import com.devdouanla.domain.SessionTest;
import com.devdouanla.service.dto.QuestionAskDTO;
import com.devdouanla.service.dto.ReponseCandidatDTO;
import com.devdouanla.service.dto.SessionTestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReponseCandidat} and its DTO {@link ReponseCandidatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReponseCandidatMapper extends EntityMapper<ReponseCandidatDTO, ReponseCandidat> {
    @Mapping(target = "questionAsk", source = "questionAsk", qualifiedByName = "questionAskId")
    @Mapping(target = "session", source = "session", qualifiedByName = "sessionTestId")
    ReponseCandidatDTO toDto(ReponseCandidat s);

    @Named("questionAskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionAskDTO toDtoQuestionAskId(QuestionAsk questionAsk);

    @Named("sessionTestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SessionTestDTO toDtoSessionTestId(SessionTest sessionTest);
}
