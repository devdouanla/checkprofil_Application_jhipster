package com.devdouanla.service.mapper;

import com.devdouanla.domain.Manager;
import com.devdouanla.domain.Poste;
import com.devdouanla.service.dto.ManagerDTO;
import com.devdouanla.service.dto.PosteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Poste} and its DTO {@link PosteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PosteMapper extends EntityMapper<PosteDTO, Poste> {
    @Mapping(target = "manager", source = "manager", qualifiedByName = "managerId")
    PosteDTO toDto(Poste s);

    @Named("managerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ManagerDTO toDtoManagerId(Manager manager);
}
