package com.devdouanla.service.mapper;

import com.devdouanla.domain.Competence;
import com.devdouanla.domain.Expert;
import com.devdouanla.service.dto.CompetenceDTO;
import com.devdouanla.service.dto.ExpertDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Competence} and its DTO {@link CompetenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompetenceMapper extends EntityMapper<CompetenceDTO, Competence> {
    @Mapping(target = "expertses", source = "expertses", qualifiedByName = "expertIdSet")
    CompetenceDTO toDto(Competence s);

    @Mapping(target = "removeExperts", ignore = true)
    Competence toEntity(CompetenceDTO competenceDTO);

    @Named("expertId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExpertDTO toDtoExpertId(Expert expert);

    @Named("expertIdSet")
    default Set<ExpertDTO> toDtoExpertIdSet(Set<Expert> expert) {
        return expert.stream().map(this::toDtoExpertId).collect(Collectors.toSet());
    }
}
