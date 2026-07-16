package com.project.hrbank.controller;

import com.project.hrbank.controller.doc.EmployeeStatsControllerDoc;
import com.project.hrbank.domain.EmployeeTrendUnit;
import com.project.hrbank.dto.response.EmployeeTrendResponse;
import com.project.hrbank.service.EmployeeStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees/stats")
public class EmployeeStatsController implements EmployeeStatsControllerDoc {

  private final EmployeeStatsService employeeStatsService;


  @Override
  @GetMapping("/trend")
  public ResponseEntity<List<EmployeeTrendResponse>> getEmployeeTrend(
      @RequestParam(required = false)
      LocalDate from,

      @RequestParam(required = false)
      LocalDate to,

      @RequestParam(required = false)
      EmployeeTrendUnit unit
  ) {

    return ResponseEntity.ok(
        employeeStatsService.getEmployeeTrend(
            from,
            to,
            unit
        )
    );
  }
}