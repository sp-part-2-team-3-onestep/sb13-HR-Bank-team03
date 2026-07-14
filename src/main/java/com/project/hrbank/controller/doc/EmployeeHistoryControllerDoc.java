package com.project.hrbank.controller.doc;



import com.project.hrbank.dto.response.CursorPageResponseChangeLogDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

public interface EmployeeHistoryControllerDoc {
    @Operation(summary = "직원 정보 수정 이력")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "조회 성공"),
            @ApiResponse(responseCode = "400",description = "잘못된 요청"),
            @ApiResponse(responseCode = "500",description = "서버 오류")
    })
    ResponseEntity<CursorPageResponseChangeLogDto> findByParam(
            @RequestParam String employeeNumber,
            @RequestParam String type,  // CREATED, UPDATED, DELETED
            @RequestParam String memo,
            @RequestParam String ipAddress,
            @RequestParam Instant atFrom, // datetime
            @RequestParam Instant atTo, // datetime
            @RequestParam Long idAfter,
            @RequestParam String cursor,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(defaultValue = "at") String sortField, // ipAddress (ip), at(시간),
            @RequestParam(defaultValue = "desc") String sortDirection
    );
}
