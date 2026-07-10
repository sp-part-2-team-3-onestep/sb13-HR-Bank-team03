package com.project.hrbank.repository;

import com.project.hrbank.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

// 아이스크림 먹고 싶다.
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(String name);

}
