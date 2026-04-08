package com.devdouanla.service.mapper;

import com.devdouanla.domain.Competence;
import com.devdouanla.domain.Expert;
import com.devdouanla.domain.User;
import com.devdouanla.service.dto.CompetenceDTO;
import com.devdouanla.service.dto.ExpertDTO;
import com.devdouanla.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Expert} and its DTO {@link ExpertDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpertMapper extends EntityMapper<ExpertDTO, Expert> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "competenceses", source = "competenceses", qualifiedByName = "competenceIdSet")
    ExpertDTO toDto(Expert s);

    @Mapping(target = "competenceses", ignore = true)
    @Mapping(target = "removeCompetences", ignore = true)
    Expert toEntity(ExpertDTO expertDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("competenceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompetenceDTO toDtoCompetenceId(Competence competence);

    @Named("competenceIdSet")
    default Set<CompetenceDTO> toDtoCompetenceIdSet(Set<Competence> competence) {
        return competence.stream().map(this::toDtoCompetenceId).collect(Collectors.toSet());
    }
}
