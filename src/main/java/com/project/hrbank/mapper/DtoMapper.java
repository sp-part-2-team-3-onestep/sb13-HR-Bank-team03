package com.project.hrbank.mapper;

import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.response.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DtoMapper {

    @Mapping(target = "name", source = "departmentName")
    @Mapping(target = "establishedDate", source = "establishedDate", dateFormat = "yyyy-MM-dd")
    DepartmentDto toDto(Department department);
}
