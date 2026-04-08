package com.devdouanla.service.mapper;

import com.devdouanla.domain.Competence;
import com.devdouanla.domain.CompetenceRequise;
import com.devdouanla.domain.Poste;
import com.devdouanla.service.dto.CompetenceDTO;
import com.devdouanla.service.dto.CompetenceRequiseDTO;
import com.devdouanla.service.dto.PosteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompetenceRequise} and its DTO {@link CompetenceRequiseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompetenceRequiseMapper extends EntityMapper<CompetenceRequiseDTO, CompetenceRequise> {
    @Mapping(target = "competence", source = "competence", qualifiedByName = "competenceId")
    @Mapping(target = "poste", source = "poste", qualifiedByName = "posteId")
    CompetenceRequiseDTO toDto(CompetenceRequise s);

    @Named("competenceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    CompetenceDTO toDtoCompetenceId(Competence competence);

    @Named("posteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "niveau", source = "niveau")
    PosteDTO toDtoPosteId(Poste poste);
}
