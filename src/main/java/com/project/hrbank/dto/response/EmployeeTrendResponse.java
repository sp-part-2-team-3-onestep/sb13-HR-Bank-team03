package com.project.hrbank.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeTrendResponse {

  private LocalDate date;
  private Long count;
  private Long change;
  private Double changeRate;
}