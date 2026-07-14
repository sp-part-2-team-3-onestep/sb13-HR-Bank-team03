package com.project.hrbank.mapper;

import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DtoMapper {

    @Mapping(target = "id", source = "historyId")
    EmployeeHistoryDetailResponse toEmployeeHistoryDetailResponse(
            EmployeeHistory employeeHistory
    );
}