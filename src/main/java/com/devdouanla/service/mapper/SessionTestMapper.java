package com.devdouanla.service.mapper;

import com.devdouanla.domain.Epreuve;
import com.devdouanla.domain.Evaluation;
import com.devdouanla.domain.Resultat;
import com.devdouanla.domain.SessionTest;
import com.devdouanla.service.dto.EpreuveDTO;
import com.devdouanla.service.dto.EvaluationDTO;
import com.devdouanla.service.dto.ResultatDTO;
import com.devdouanla.service.dto.SessionTestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SessionTest} and its DTO {@link SessionTestDTO}.
 */
@Mapper(componentModel = "spring")
public interface SessionTestMapper extends EntityMapper<SessionTestDTO, SessionTest> {
    @Mapping(target = "resultat", source = "resultat", qualifiedByName = "resultatId")
    @Mapping(target = "evaluation", source = "evaluation", qualifiedByName = "evaluationId")
    @Mapping(target = "epreuves", source = "epreuves", qualifiedByName = "epreuveId")
    SessionTestDTO toDto(SessionTest s);

    @Named("resultatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResultatDTO toDtoResultatId(Resultat resultat);

    @Named("evaluationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EvaluationDTO toDtoEvaluationId(Evaluation evaluation);

    @Named("epreuveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EpreuveDTO toDtoEpreuveId(Epreuve epreuve);
}
