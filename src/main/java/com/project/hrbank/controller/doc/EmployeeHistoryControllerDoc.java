package com.project.hrbank.controller.doc;

import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import com.project.hrbank.dto.response.EmployeeHistoryDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

public interface EmployeeHistoryControllerDoc {

    @Operation(summary = "직원 정보 수정 이력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<CursorPageResponseChangeLogDto> findByParam(
            @RequestParam String employeeNumber,
            @RequestParam String type,
            @RequestParam String memo,
            @RequestParam String ipAddress,
            @RequestParam Instant atFrom,
            @RequestParam Instant atTo,
            @RequestParam Long idAfter,
            @RequestParam String cursor,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "at") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection
    );

    @Operation(summary = "직원 정보 수정 이력 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "수정 이력 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    EmployeeHistoryDetailResponse findById(Long id);
}