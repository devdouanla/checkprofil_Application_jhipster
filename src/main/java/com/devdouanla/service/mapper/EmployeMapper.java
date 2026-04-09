package com.devdouanla.service.mapper;

import com.devdouanla.domain.Employe;
import com.devdouanla.domain.Poste;
import com.devdouanla.service.dto.EmployeDTO;
import com.devdouanla.service.dto.PosteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employe} and its DTO {@link EmployeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeMapper extends EntityMapper<EmployeDTO, Employe> {
    @Mapping(target = "poste", source = "poste", qualifiedByName = "posteId")
    EmployeDTO toDto(Employe s);

    @Named("posteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom") // Ignore questions to prevent circular reference
    PosteDTO toDtoPosteId(Poste poste);
}
