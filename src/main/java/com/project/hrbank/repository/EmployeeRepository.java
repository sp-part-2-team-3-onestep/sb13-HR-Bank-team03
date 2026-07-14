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

import java.time.Instant;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    int countByDepartmentId(Long departmentId);

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


    boolean existsByEmail(String email);


    // 백업을 위해 페이징 단위로 나누어 쿼리하는 함수
    @EntityGraph()
    Page<Employee> findAllBy(Pageable pageable);
}
