package com.project.hrbank.service;

import com.project.hrbank.domain.Department;
import com.project.hrbank.domain.Employee;
import com.project.hrbank.domain.EmployeeHistory;
import com.project.hrbank.domain.EmployeeStatus;
import com.project.hrbank.domain.FileMeta;
import com.project.hrbank.infra.Structure;
import com.project.hrbank.mapper.DtoMapper;
import com.project.hrbank.repository.DepartmentRepository;
import com.project.hrbank.repository.EmployeeHistoryRepository;
import com.project.hrbank.repository.EmployeeRepository;
import com.project.hrbank.repository.FileMetaRepository;
import com.project.hrbank.service.basic.BasicEmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeHistoryRepository employeeHistoryRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private FileMetaRepository fileMetaRepository;

    @Mock
    private Structure structure;

    @Mock
    private DtoMapper mapper;

    @InjectMocks
    private BasicEmployeeService employeeService;

    @Test
    @DisplayName("fromDate, toDate가 둘 다 없으면 null, null 그대로 리포지토리에 전달")
    void countEmployees_noDateFilter() {
        when(employeeRepository.countByStatusAndHireDateRange(
                any(),
                any(),
                any()
        )).thenReturn(10L);

        long count = employeeService.countEmployees(null, null, null);

        assertThat(count).isEqualTo(10L);

        verify(employeeRepository).countByStatusAndHireDateRange(
                null,
                null,
                null
        );
    }

    @Test
    @DisplayName("fromDate만 있으면 toDate는 현재 시각으로 채워서 전달")
    void countEmployees_fromDateOnly() {
        when(employeeRepository.countByStatusAndHireDateRange(
                any(),
                any(),
                any()
        )).thenReturn(3L);

        Instant before = Instant.now();

        employeeService.countEmployees(
                null,
                LocalDate.of(2024, 1, 1),
                null
        );

        Instant after = Instant.now();

        ArgumentCaptor<Instant> toCaptor =
                ArgumentCaptor.forClass(Instant.class);

        verify(employeeRepository).countByStatusAndHireDateRange(
                isNull(),
                eq(Instant.parse("2024-01-01T00:00:00Z")),
                toCaptor.capture()
        );

        assertThat(toCaptor.getValue()).isBetween(before, after);
    }

    @Test
    @DisplayName("fromDate, toDate가 모두 있으면 toDate는 다음날 자정으로 변환된다")
    void countEmployees_bothDates() {
        when(employeeRepository.countByStatusAndHireDateRange(
                any(),
                any(),
                any()
        )).thenReturn(5L);

        employeeService.countEmployees(
                EmployeeStatus.ACTIVE,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );

        verify(employeeRepository).countByStatusAndHireDateRange(
                EmployeeStatus.ACTIVE,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-02-01T00:00:00Z")
        );
    }

    @Test
    @DisplayName("toDate만 있으면 fromDate가 없어도 toDate는 다음날 자정으로 변환")
    void countEmployees_toDateOnly() {
        when(employeeRepository.countByStatusAndHireDateRange(
                any(),
                any(),
                any()
        )).thenReturn(7L);

        employeeService.countEmployees(
                null,
                null,
                LocalDate.of(2024, 1, 31)
        );

        verify(employeeRepository).countByStatusAndHireDateRange(
                null,
                null,
                Instant.parse("2024-02-01T00:00:00Z")
        );
    }

    @Test
    @DisplayName("프로필 이미지가 있으면 파일과 FileMeta를 함께 삭제한다")
    void deleteEmployee_withProfileImage() {
        Employee employee = mock(Employee.class);
        FileMeta fileMeta = mock(FileMeta.class);
        Department department = mock(Department.class);

        when(employee.getProfileImaged()).thenReturn(fileMeta);
        when(employee.getDepartment()).thenReturn(department);
        when(fileMeta.getFileName()).thenReturn("uuid_profile.png");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        // save()는 저장된 Employee를 반환해야 함
        when(employeeRepository.save(employee))
                .thenReturn(employee);

        employeeService.deleteEmployee(1L, "127.0.0.1");

        verify(employeeRepository).save(employee);
        verify(employeeHistoryRepository).save(any(EmployeeHistory.class));
        verify(structure).delete("uuid_profile.png");
        verify(fileMetaRepository).delete(fileMeta);
    }

    @Test
    @DisplayName("프로필 이미지가 없으면 파일과 FileMeta 삭제를 시도하지 않는다")
    void deleteEmployee_withoutProfileImage() {
        Employee employee = mock(Employee.class);
        Department department = mock(Department.class);

        when(employee.getProfileImaged()).thenReturn(null);
        when(employee.getDepartment()).thenReturn(department);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        // save() 결과가 null이면 서비스의 emp.getDepartment()에서 NPE 발생
        when(employeeRepository.save(employee))
                .thenReturn(employee);

        employeeService.deleteEmployee(1L, "127.0.0.1");

        verify(employeeRepository).save(employee);
        verify(employeeHistoryRepository).save(any(EmployeeHistory.class));
        verify(structure, never()).delete(any());
        verify(fileMetaRepository, never()).delete(any());
    }
}