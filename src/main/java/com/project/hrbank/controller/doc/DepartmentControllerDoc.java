package com.project.hrbank.controller.doc;


import com.project.hrbank.dto.request.DepartmentCreateRequest;
import com.project.hrbank.dto.request.DepartmentUpdateRequest;
import com.project.hrbank.dto.response.DepartmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface DepartmentControllerDoc {

    @Operation(
            summary = "부서 생성",
            description = "부서 생성."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "파라매터 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),

    })
    ResponseEntity<DepartmentDto> create(
            @RequestBody DepartmentCreateRequest request
    );

    @Operation(
            summary = "부서 수정",
            description = "부서 수정"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "파라매터 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "부서 id 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),

    })
    ResponseEntity<DepartmentDto> update(
            @PathVariable Long id,
            @RequestBody DepartmentUpdateRequest request
    );

    @Operation(
            summary = "부서 상세 조회",
            description = "부서 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<DepartmentDto> findById(
            @Parameter(description = "부서 ID", required = true) @PathVariable Long id);

}