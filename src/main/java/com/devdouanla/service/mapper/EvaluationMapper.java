package com.devdouanla.service.mapper;

import com.devdouanla.domain.Employe;
import com.devdouanla.domain.Evaluation;
import com.devdouanla.service.dto.EmployeDTO;
import com.devdouanla.service.dto.EvaluationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evaluation} and its DTO {@link EvaluationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EvaluationMapper extends EntityMapper<EvaluationDTO, Evaluation> {
    @Mapping(target = "employe", source = "employe", qualifiedByName = "employeId")
    EvaluationDTO toDto(Evaluation s);

    @Named("employeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeDTO toDtoEmployeId(Employe employe);
}
