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

}