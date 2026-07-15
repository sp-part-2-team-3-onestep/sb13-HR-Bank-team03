package com.project.hrbank.repository;

import com.project.hrbank.domain.Department;
import com.project.hrbank.dto.request.DepartmentSearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>, PagingRepository {

    boolean existsByDepartmentName(String name);

//    // 커서 기반 페이징 (부서생성일 내림차순) - deletedAt IS NULL 조건 포함[cite: 2]
//    @Query("SELECT d FROM Department d " +
//            "WHERE d.deletedAt IS NULL " +
//            "AND (:keyword IS NULL OR :keyword = '' " +
//            "     OR LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "     OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//            "AND (:cursorDate IS NULL OR d.establishedDate < :cursorDate " +
//            "     OR (d.establishedDate = :cursorDate AND d.id < :cursorId)) " +
//            "ORDER BY d.establishedDate DESC, d.id DESC")
//    List<Department> searchByCursor(
//            @Param("keyword") String keyword,
//            @Param("cursorDate") LocalDate cursorDate,
//            @Param("cursorId") Long cursorId,
//            Pageable pageable
//    );
//
//    // 전체 개수 카운트 (totalElements 용)
//    @Query("SELECT COUNT(d) FROM Department d " +
//            "WHERE d.deletedAt IS NULL " +
//            "AND (:keyword IS NULL OR :keyword = '' " +
//            "     OR LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "     OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
//    long countDepartments(@Param("keyword") String keyword);
}