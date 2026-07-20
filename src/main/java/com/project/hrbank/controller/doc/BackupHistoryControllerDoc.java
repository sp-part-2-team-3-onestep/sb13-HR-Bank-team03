package com.project.hrbank.controller.doc;


import com.project.hrbank.dto.response.BackupDto;
import com.project.hrbank.dto.response.CursorPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

public interface BackupHistoryControllerDoc {


    @Operation(
            summary = "백업 생성",
            description = "현재 직원 정보를 csv 파일로 백업 생성."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "백업 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),

    })
    ResponseEntity<BackupDto> create(
            HttpServletRequest request
    );

    @Operation(
            summary = "최근 백업 조회",
            description = "가장 최근의 백업 정보를 조회."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "요청 파라매터 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),

    })
    ResponseEntity<BackupDto> latest(
            @RequestParam(defaultValue = "COMPLETED") String status
    );

    @Operation(
            summary = "백업 조회",
            description = "시행한 백업들의 조회 목록을 반환."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "요청 파라매터 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),

    })
    ResponseEntity<CursorPageResponse<BackupDto>> list(
            @RequestParam(required = false) String worker,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Instant startedAtFrom,
            @RequestParam(required = false) Instant startedAtTo,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "startedAt") String sortField,
            @RequestParam(defaultValue = "DESC") String sortDirection
    );
}