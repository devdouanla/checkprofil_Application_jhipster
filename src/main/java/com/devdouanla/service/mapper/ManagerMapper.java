package com.devdouanla.service.mapper;

import com.devdouanla.domain.Manager;
import com.devdouanla.domain.User;
import com.devdouanla.service.dto.ManagerDTO;
import com.devdouanla.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Manager} and its DTO {@link ManagerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManagerMapper extends EntityMapper<ManagerDTO, Manager> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    ManagerDTO toDto(Manager s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
