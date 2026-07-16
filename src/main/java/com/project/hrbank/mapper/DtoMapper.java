package com.project.hrbank.mapper;

import com.project.hrbank.domain.*;
import com.project.hrbank.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

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
    @Mapping(target = "hireDate", source = "hireDate", dateFormat = "yyyy-MM-dd")
    EmployeeDto toDto(Employee employee);

    @Mapping(target = "employeeNumber", source = "employee.employeeNumber")
    @Mapping(target = "type",expression = "java(employeeHistory.getTypeString())")
    @Mapping(target = "at", source = "createAt")
    ChangeLogDto toDto(EmployeeHistory employeeHistory);

    @Mapping(target = "id", source = "employeeHistory.id")
    @Mapping(target = "type", source = "employeeHistory.type")
    @Mapping(target = "employeeNumber", source = "employeeHistory.employee.employeeNumber")
    @Mapping(target = "memo", source = "employeeHistory.memo")
    @Mapping(target = "ipAddress", source = "employeeHistory.ipAddress")
    @Mapping(target = "at", source = "employeeHistory.createdAt")
    @Mapping(target = "employeeName", source = "employeeHistory.employee.name")
    @Mapping(target = "profileImageId", source = "employeeHistory.employee.profileImaged.id")
    @Mapping(target = "diffs", source = "diffDto")
    EmployeeHistoryDetailResponse toEmployeeHistoryDetailResponse(
            EmployeeHistory employeeHistory, List<DiffDto> diffDto
    );


    @Mapping(target = "worker", source = "ip")
    @Mapping(target = "startedAt", source = "startTime")
    @Mapping(target = "endedAt", source = "endTime")
    @Mapping(target = "status", expression = "java(backup.status())")
    @Mapping(target = "fileId", source = "fileMeta.id")
    BackupDto toDto(BackupHistory backup);


    @Mapping(target = "department", source = "department.departmentName")
    @Mapping(target = "hireDate", source = "hireDate", dateFormat = "yyyy-MM-dd")
    BackupCsvDTO toCsvDTO(Employee employee);


    DiffDto toDto(Diff diff);


}
