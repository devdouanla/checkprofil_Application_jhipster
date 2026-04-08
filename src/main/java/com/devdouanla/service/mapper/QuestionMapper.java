package com.devdouanla.service.mapper;

import com.devdouanla.domain.Epreuve;
import com.devdouanla.domain.Question;
import com.devdouanla.service.dto.EpreuveDTO;
import com.devdouanla.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "epreuve", source = "epreuve", qualifiedByName = "epreuveId")
    QuestionDTO toDto(Question s);

    @Named("epreuveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EpreuveDTO toDtoEpreuveId(Epreuve epreuve);
}
