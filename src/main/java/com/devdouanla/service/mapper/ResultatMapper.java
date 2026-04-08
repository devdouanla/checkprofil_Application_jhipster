package com.devdouanla.service.mapper;

import com.devdouanla.domain.Resultat;
import com.devdouanla.service.dto.ResultatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resultat} and its DTO {@link ResultatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResultatMapper extends EntityMapper<ResultatDTO, Resultat> {}
