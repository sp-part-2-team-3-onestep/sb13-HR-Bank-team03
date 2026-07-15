package com.project.hrbank.repository;

import com.project.hrbank.domain.Department;
import com.project.hrbank.domain.Employee;
import com.project.hrbank.domain.EmployeeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @MockBean
    private JPAQueryFactory jpaQueryFactory;

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private EmployeeRepository employeeRepository;

    @BeforeEach
    @DisplayName("시작 전 데이터 넣기")
    void beforeEach() {
        Department department = departmentRepository.save(new Department("개발팀", "개발팀입니다", LocalDate.now()));

        Employee employee1 = Employee.create(
                "김철수",
                department,
                "001",
                "kim@test.com",
                "사원",
                Instant.parse("2024-01-10T00:00:00Z"),
                EmployeeStatus.ACTIVE,
                null
        );

        Employee employee2 = Employee.create(
                "이영희",
                department,
                "002",
                "lee@test.com",
                "사원",
                Instant.parse("2024-06-15T00:00:00Z"),
                EmployeeStatus.ACTIVE,
                null
        );

        Employee employee3 = Employee.create(
                "박민수",
                department,
                "003",
                "park@test.com",
                "대리",
                Instant.parse("2024-03-20T00:00:00Z"),
                EmployeeStatus.ON_LEAVE,
                null
        );

        Employee employee4 = Employee.create(
                "최지훈",
                department,
                "004",
                "choi@test.com",
                "과장",
                Instant.parse("2023-12-01T00:00:00Z"),
                EmployeeStatus.RESIGNED,
                null
        );

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        employeeRepository.save(employee4);
    }

    @Test
    @DisplayName("전체 직원 수 반환")
    void countAll() {
        long count = employeeRepository.countByStatusAndHireDateRange(null, null, null);
        assertThat(count).isEqualTo(4); // 전체 사원 수
    }

    @Test
    @DisplayName("status로 필터링")
    void countByStatus() {
        long count = employeeRepository.countByStatusAndHireDateRange(EmployeeStatus.ACTIVE, null, null);
        assertThat(count).isEqualTo(2); // EmployeeStatus.ACTIVE
    }

    @Test
    @DisplayName("fromDate 이후 입사자만 필터링")
    void countFromDate() {
        long count = employeeRepository.countByStatusAndHireDateRange(
                null, Instant.parse("2024-01-01T00:00:00Z"), null);
        assertThat(count).isEqualTo(3); // 김철수, 이영희, 박민수
    }

    @Test
    @DisplayName("fromDate-toDate 범위 안의 입사자만 필터링")
    void countDateRange() {
        long count = employeeRepository.countByStatusAndHireDateRange(
                null,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-04-01T00:00:00Z"));
        assertThat(count).isEqualTo(2); // 김철수, 박민수
    }

    @Test
    @DisplayName("status와 날짜 조건을 함께 적용")
    void countByStatusAndDateRange() {
        long count = employeeRepository.countByStatusAndHireDateRange(
                EmployeeStatus.ACTIVE,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-12-31T00:00:00Z"));
        assertThat(count).isEqualTo(2); // 김철수, 이영희
    }
}