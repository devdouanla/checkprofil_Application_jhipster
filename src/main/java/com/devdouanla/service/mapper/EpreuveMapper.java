package com.devdouanla.service.mapper;

import com.devdouanla.domain.Competence;
import com.devdouanla.domain.Epreuve;
import com.devdouanla.service.dto.CompetenceDTO;
import com.devdouanla.service.dto.EpreuveDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Epreuve} and its DTO {@link EpreuveDTO}.
 */
@Mapper(componentModel = "spring")
public interface EpreuveMapper extends EntityMapper<EpreuveDTO, Epreuve> {
    @Mapping(target = "competence", source = "competence", qualifiedByName = "competenceId")
    EpreuveDTO toDto(Epreuve s);

    @Named("competenceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    CompetenceDTO toDtoCompetenceId(Competence competence);
}
