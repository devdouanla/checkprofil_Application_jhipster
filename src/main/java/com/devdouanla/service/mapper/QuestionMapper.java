package com.devdouanla.service.mapper;

import com.devdouanla.domain.Competence;
import com.devdouanla.domain.Question;
import com.devdouanla.service.dto.CompetenceDTO;
import com.devdouanla.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "competence", source = "competence", qualifiedByName = "competenceId")
    QuestionDTO toDto(Question s);

    @Named("competenceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompetenceDTO toDtoCompetenceId(Competence competence);
}
