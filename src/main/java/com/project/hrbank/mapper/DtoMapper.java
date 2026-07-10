package com.project.hrbank.mapper;

import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.response.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DtoMapper {

    @Mapping(target = "name", source = "department.departmentName")
    @Mapping(target = "description", source = "department.description")
    @Mapping(target = "establishedDate", source = "department.establishedDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "employeeCount", source = "employeesCounter", defaultValue = "0")
    DepartmentDto toDto(Department department, Integer employeesCounter);
}
