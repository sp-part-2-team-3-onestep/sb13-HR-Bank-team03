package com.project.hrbank.controller.doc;

import com.project.hrbank.dto.request.EmployeeCreateRequest;
import com.project.hrbank.dto.request.EmployeeSearchRequest;
import com.project.hrbank.dto.response.CursorPageResponse;
import com.project.hrbank.dto.response.EmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeControllerDoc {

    @Operation(summary = "직원 생성")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "직원 생성 성공"),
        @ApiResponse(responseCode = "400",description = "직원 정보 오류"),
        @ApiResponse(responseCode = "404",description = "부서 없음")
    })
    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<EmployeeDto> create(
        @Parameter(content = @Content(mediaType = "application/json"))
        @RequestPart(name = "employee") EmployeeCreateRequest request,
        @RequestPart(name = "profile") MultipartFile file
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
}