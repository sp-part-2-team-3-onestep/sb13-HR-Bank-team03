package com.project.hrbank.service;

import com.project.hrbank.domain.Employee;
import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.domain.FileMeta;
import com.project.hrbank.infra.Structure;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.repository.FileMetaRepository;
import com.project.hrbank.service.basic.BasicEmployeeService;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private FileMetaRepository fileMetaRepository;

    @Mock
    private Structure structure;

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

    @Test
    @DisplayName("프로필 이미지가 있으면 파일과 FileMeta를 함께 삭제한다")
    void deleteEmployee_withProfileImage() {
        Employee employee = mock(Employee.class); // Employee 껍데기 객체 생성
        FileMeta fileMeta = mock(FileMeta.class); // FileMeta 껍데기 객체 생성

        // employee.getProfileImaged()를 호출하면 fileMeta를 return
        when(employee.getProfileImaged()).thenReturn(fileMeta);
        /*
        fileMeta.getFileName()을 호출하면 uuid_profile.png를 stub
        structure.delete() 호출할 때 uuid_profile.png로 넘어가는지 확인
        */
        when(fileMeta.getFileName()).thenReturn("uuid_profile.png");
        // repo한테 id=1로 조회 시 employee가 있다. -> employeeRepository.findById(id).orElseThrow()
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // deleteEmployee() 메서드 실행
        employeeService.deleteEmployee(1L);

        // uuid_profile.png 호출됐는지 검증
        verify(structure).delete("uuid_profile.png");
        // delete()가 호출됐는지 검증
        verify(fileMetaRepository).delete(fileMeta);
    }

    @Test
    @DisplayName("프로필 이미지가 없으면 파일/FileMeta 삭제를 시도하지 않는다")
    void deleteEmployee_withoutProfileImage() {
        Employee employee = mock(Employee.class);
        when(employee.getProfileImaged()).thenReturn(null);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        // FileMeta 삭제가 한 번도 호출되지 않았는가 확인 : never() -> times(0)
        verify(structure, never()).delete(any());
        verify(fileMetaRepository, never()).delete(any());
    }

}