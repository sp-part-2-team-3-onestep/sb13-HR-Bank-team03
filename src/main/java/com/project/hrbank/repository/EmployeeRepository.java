package com.project.hrbank.repository;

import com.project.hrbank.domain.Employee;
import com.project.hrbank.domain.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.time.Instant;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeePagingRepository {

    int countByDepartmentIdAndDeletedAtIsNull(Long departmentId);

    @Query("""
        SELECT COUNT(e) FROM Employee e
        WHERE (:status IS NULL OR e.status = :status)
        AND (:fromDate IS NULL OR e.hireDate >= :fromDate)
        AND (:toDate IS NULL OR e.hireDate < :toDate)
        """)
    long countByStatusAndHireDateRange(
            @Param("status") EmployeeStatus status,
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate
    );

    @Query("""
    SELECT e.department.departmentName, COUNT(e)
    FROM Employee e
    WHERE e.status = :status
    GROUP BY e.department.departmentName
    ORDER BY e.department.departmentName
    """)
    List<Object[]> countGroupByDepartment(
            @Param("status") EmployeeStatus status
    );

    @Query("""
    SELECT e.position, COUNT(e)
    FROM Employee e
    WHERE e.status = :status
    GROUP BY e.position
    ORDER BY e.position
    """)
    List<Object[]> countGroupByPosition(
            @Param("status") EmployeeStatus status
    );

    boolean existsByEmail(String email);

    // 여기 QueryDSL로 변경 가능 할 수 있음
    @Query("""
        select e.createAt
        from Employee e
        where e.createAt <= :to
        order by e.createAt
        """)
    List<Instant> findCreatedDates(
        @Param("to") Instant to
    );


    // 백업을 위해 페이징 단위로 나누어 쿼리하는 함수
    @EntityGraph()
    Page<Employee> findAllBy(Pageable pageable);
}
