package com.project.hrbank.service;

import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.service.basic.BasicEmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private BasicEmployeeService employeeService;

    @Test
    @DisplayName("fromDate, toDate가 둘 다 없으면 null, null 그대로 리포지토리에 전달")
    void countEmployees_noDateFilter() {
        when(employeeRepository.countByStatusAndHireDateRange(any(), any(), any())).thenReturn(10L);

        long count = employeeService.countEmployees(null, null, null);

        assertThat(count).isEqualTo(10L);
        verify(employeeRepository).countByStatusAndHireDateRange(null, null, null);
    }

    @Test
    @DisplayName("fromDate만 있으면 toDate는 현재 시각으로 채워서 전달")
    void countEmployees_fromDateOnly() {
        when(employeeRepository.countByStatusAndHireDateRange(any(), any(), any())).thenReturn(3L);

        Instant before = Instant.now();
        employeeService.countEmployees(null, LocalDate.of(2024, 1, 1), null);
        Instant after = Instant.now();

        ArgumentCaptor<Instant> toCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(employeeRepository).countByStatusAndHireDateRange(
                isNull(),
                eq(Instant.parse("2024-01-01T00:00:00Z")),
                toCaptor.capture());

        assertThat(toCaptor.getValue()).isBetween(before, after);
    }

    @Test
    @DisplayName("fromDate, toDate가 모두 있으면 toDate는 다음날 자정(exclusive)으로 변환된다")
    void countEmployees_bothDates() {
        when(employeeRepository.countByStatusAndHireDateRange(any(), any(), any())).thenReturn(5L);

        employeeService.countEmployees(EmployeeStatus.ACTIVE, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        verify(employeeRepository).countByStatusAndHireDateRange(
                EmployeeStatus.ACTIVE,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-02-01T00:00:00Z"));
    }

    @Test
    @DisplayName("toDate만 있으면 fromDate가 없어도 toDate는 다음날 자정으로 변환")
    void countEmployees_toDateOnly() {
        when(employeeRepository.countByStatusAndHireDateRange(any(), any(), any())).thenReturn(7L);

        employeeService.countEmployees(null, null, LocalDate.of(2024, 1, 31));

        verify(employeeRepository).countByStatusAndHireDateRange(
                null, null, Instant.parse("2024-02-01T00:00:00Z"));
    }
}