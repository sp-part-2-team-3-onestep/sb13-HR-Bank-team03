package com.project.hrbank.mapper;

import com.project.hrbank.domain.Department;
import com.project.hrbank.domain.Employee;
import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.dto.response.ChangeLogDto;
import com.project.hrbank.dto.response.DepartmentDto;
import com.project.hrbank.dto.response.EmployeeDto;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DtoMapper {

    @Mapping(target = "name", source = "department.departmentName")
    @Mapping(target = "description", source = "department.description")
    @Mapping(target = "establishedDate", source = "department.establishedDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "employeeCount", source = "employeesCounter", defaultValue = "0")
    DepartmentDto toDto(Department department, Integer employeesCounter);

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.departmentName")
    @Mapping(target = "profileImageId", source = "profileImaged.id")
    @Mapping(
        target = "hireDate",
        expression = "java(employee.getHireDate().atZone(java.time.ZoneOffset.UTC).format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy년 M월 d일\")))"
    )
    EmployeeDto toDto(Employee employee);

    @Mapping(target = "employeeNumber", source = "employee.employeeNumber")
    @Mapping(target = "type", source = "employee.status")
    @Mapping(target = "at", source = "createAt")
    ChangeLogDto toDto(EmployeeHistory employeeHistory);

    @Mapping(target = "id", source = "historyId")
    EmployeeHistoryDetailResponse toEmployeeHistoryDetailResponse(
            EmployeeHistory employeeHistory
    );
}