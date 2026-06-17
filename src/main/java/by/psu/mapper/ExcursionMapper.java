package by.psu.mapper;

import by.psu.dto.request.ExcursionCreateRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.model.Excursion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ERROR)
public interface ExcursionMapper {
    @Mapping(target = "id", ignore = true)
    Excursion toEntity(ExcursionCreateRequest request);

    ExcursionResponse toResponse(Excursion excursion);
}
