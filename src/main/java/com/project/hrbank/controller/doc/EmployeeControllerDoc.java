package com.project.hrbank.controller.doc;

import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.request.EmployeeSearchRequest;
import com.project.hrbank.dto.request.EmployeeUpdateRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.response.EmployeeDistributionDto;
import com.project.hrbank.dto.response.EmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeControllerDoc {

    @Operation(summary = "직원 생성")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "직원 생성 성공"),
        @ApiResponse(responseCode = "400",description = "직원 정보 오류"),
        @ApiResponse(responseCode = "404",description = "부서 없음")
    })
    ResponseEntity<EmployeeDto> create(
        @Parameter(content = @Content(mediaType = "application/json"))
        @RequestPart(name = "employee") EmployeeCreateRequest request,
        @RequestPart(name = "profile") MultipartFile file,
        HttpServletRequest req
    );

    @Operation(summary = "직원 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "직원 목록 조회 성공"
        )
    })
    @GetMapping("")
    ResponseEntity<CursorPageResponse<EmployeeDto>> getEmployees(
        EmployeeSearchRequest searchRequest
    );

    @Operation(summary = "직원 분포 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "직원 분포 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 그룹화 기준"
            )
    })
    ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
            @RequestParam(defaultValue = "department") String groupBy,
            @RequestParam(defaultValue = "ACTIVE") EmployeeStatus status
    );

    @Operation(summary = "직원 수 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "직원 수 조회 성공")
    })
    ResponseEntity<Long> countEmployees(
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    );

    @Operation(summary = "직원 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "직원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "직원 없음")
    })
    ResponseEntity<Void> deleteEmployee(
            HttpServletRequest request,
            @PathVariable Long id
    );

    @Operation(summary = "직원 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "직원 수정 성공"),
            @ApiResponse(responseCode = "400", description = "직원 정보 오류"),
            @ApiResponse(responseCode = "404", description = "직원 없음")
    })
    ResponseEntity<EmployeeDto> updateEmployee(
            HttpServletRequest request,
            @PathVariable Long id,
            @Parameter(content = @Content(mediaType = "application/json"))
            @RequestPart(name = "employee") EmployeeUpdateRequest updateRequest,
            @RequestPart(name = "profile", required = false) MultipartFile file
    );
}