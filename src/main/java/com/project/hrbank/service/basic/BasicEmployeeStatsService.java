package com.project.hrbank.service.basic;

import com.project.hrbank.domain.EmployeeTrendUnit;
import com.project.hrbank.dto.response.EmployeeTrendResponse;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.service.EmployeeStatsService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicEmployeeStatsService implements EmployeeStatsService {

  private final EmployeeRepository employeeRepository;

  //
  @Override
  public List<EmployeeTrendResponse> getEmployeeTrend(
      LocalDate from,
      LocalDate to,
      EmployeeTrendUnit unit
  ) {

    if (unit == null) {
      unit = EmployeeTrendUnit.MONTH;
    }

    if (to == null) {
      to = LocalDate.now();
    }

    if (from == null) {
      from = calculateDefaultFrom(to, unit);
    }

    List<LocalDate> periods =
        createPeriods(from, unit);

    List<Instant> createdDates =
        employeeRepository.findCreatedDates(
            to.plusDays(1)
                .atStartOfDay(
                    ZoneId.systemDefault()
                )
                .toInstant()
        );

    List<LocalDate> createdLocalDates =
        createdDates.stream()
            .map(createAt ->
                createAt
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
            .toList();


    return buildTrendResponse(
            periods,
            createdLocalDates
    );
  }

  //
  private LocalDate calculateDefaultFrom(
      LocalDate to,
      EmployeeTrendUnit unit
  ) {

    return switch (unit) {

      case DAY ->
          to.minusDays(12);

      case WEEK ->
          to.minusWeeks(12);

      case MONTH ->
          to.minusMonths(12);

      case QUARTER ->
          to.minusMonths(36);

      case YEAR ->
          to.minusYears(12);
    };
  }

  //
  private List<LocalDate> createPeriods(
      LocalDate from,
      EmployeeTrendUnit unit
  ) {

    List<LocalDate> periods = new ArrayList<>();

    LocalDate current = from;

    for (int i = 0; i < 13; i++) {

      periods.add(current);

      current = switch (unit) {

        case DAY ->
            current.plusDays(1);

        case WEEK ->
            current.plusWeeks(1);

        case MONTH ->
            current.plusMonths(1);

        case QUARTER ->
            current.plusMonths(3);

        case YEAR ->
            current.plusYears(1);
      };
    }

    return periods;
  }

  //
  private List<EmployeeTrendResponse> buildTrendResponse(
      List<LocalDate> periods,
      List<LocalDate> createdDates
  ) {

    List<EmployeeTrendResponse> responses =
        new ArrayList<>();

    int employeeIndex = 0;

    long count = 0;

    long previousCount = 0;


    for (int i = 0; i < periods.size(); i++) {

      LocalDate period = periods.get(i);


      while (
          employeeIndex < createdDates.size()
              &&
              !createdDates.get(employeeIndex)
                  .isAfter(period)
      ) {

        count++;
        employeeIndex++;
      }


      long change =
          i == 0
              ? 0
              : count - previousCount;


      double changeRate = 0.0;


      if (i != 0 && previousCount != 0) {

        changeRate =
            ((double) change / previousCount)
                * 100;
      }


      responses.add(
          new EmployeeTrendResponse(
              period,
              count,
              change,
              changeRate
          )
      );


      previousCount = count;
    }


    return responses;
  }

}