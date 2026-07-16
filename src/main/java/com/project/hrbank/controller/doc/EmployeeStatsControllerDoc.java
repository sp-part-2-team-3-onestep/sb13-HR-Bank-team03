package com.project.hrbank.controller.doc;

import com.project.hrbank.domain.EmployeeTrendUnit;
import com.project.hrbank.dto.response.EmployeeTrendResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeStatsControllerDoc {

  @Operation(summary = "직원 수 추이 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "직원 수 추이 조회 성공"
      )
  })
  ResponseEntity<List<EmployeeTrendResponse>> getEmployeeTrend(

      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate from,

      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate to,

      @RequestParam(required = false)
      EmployeeTrendUnit unit
  );
}