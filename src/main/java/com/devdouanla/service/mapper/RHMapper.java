package com.devdouanla.service.mapper;

import com.devdouanla.domain.RH;
import com.devdouanla.domain.User;
import com.devdouanla.service.dto.RHDTO;
import com.devdouanla.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RH} and its DTO {@link RHDTO}.
 */
@Mapper(componentModel = "spring")
public interface RHMapper extends EntityMapper<RHDTO, RH> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    RHDTO toDto(RH s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
