package com.project.hrbank.controller.doc;

import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface EmployeeHistoryControllerDoc {

    @Operation(summary = "직원 정보 수정 이력 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "직원 정보 수정 이력 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "직원 정보 수정 이력을 찾을 수 없음")
    })
    EmployeeHistoryDetailResponse findById(Long id);

}