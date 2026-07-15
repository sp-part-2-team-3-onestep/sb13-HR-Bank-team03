package com.project.hrbank.repository;

import com.project.hrbank.domain.Employee;
import com.project.hrbank.dto.request.EmployeeSearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeePagingRepository {

  private final EntityManager em;

  @Override
  public List<Employee> searchByCursor(EmployeeSearchRequest request) {

    StringBuilder jpql =
        new StringBuilder(
            "SELECT e FROM Employee e " +
                "WHERE e.deletedAt IS NULL "
        );
    String field;

    // 이름 또는 이메일 검색
    if (StringUtils.hasText(request.keyword())) {
      jpql.append("""
        AND (
            LOWER(e.name) LIKE LOWER(:keyword)
            OR LOWER(e.email) LIKE LOWER(:keyword)
        )
        """);
    }

// 상태
    if (request.status() != null) {
      jpql.append("""
        AND e.status = :status
        """);
    }

// 사원번호
    if (StringUtils.hasText(request.employeeNumber())) {
      jpql.append("""
        AND LOWER(e.employeeNumber)
        LIKE LOWER(CONCAT('%', :employeeNumber, '%'))
        """);
    }

// 부서명
    if (StringUtils.hasText(request.departmentName())) {
      jpql.append("""
        AND LOWER(e.department.departmentName)
        LIKE LOWER(CONCAT('%', :departmentName, '%'))
        """);
    }

// 직함
    if (StringUtils.hasText(request.position())) {
      jpql.append("""
        AND LOWER(e.position)
        LIKE LOWER(CONCAT('%', :position, '%'))
        """);
    }

// 입사 시작일
    if (request.hireDateFrom() != null) {
      jpql.append("""
        AND e.hireDate >= :hireDateFrom
        """);
    }

// 입사 종료일
    if (request.hireDateTo() != null) {
      jpql.append("""
        AND e.hireDate < :hireDateTo
        """);
    }


    // 정렬 기준
    if ("name".equalsIgnoreCase(request.sortField())) {
      field = "e.name";
    } else if ("employeeNumber"
        .equalsIgnoreCase(request.sortField())) {
      field = "e.employeeNumber";
    } else {
      field = "e.hireDate";
    }

    String direction =
        "asc".equalsIgnoreCase(request.sortDirection())
            ? "ASC"
            : "DESC";



    // Cursor 조건
    boolean hasCursor =
        StringUtils.hasText(request.cursor())
            && request.idAfter() != null;

    if (hasCursor) {
      String operator =
          "ASC".equals(direction)
              ? ">"
              : "<";

      jpql.append("AND (")
          .append(field)
          .append(" ")
          .append(operator)
          .append(" :cursor ")
          .append("OR (")
          .append(field)
          .append(" = :cursor ")
          .append("AND e.id ")
          .append(operator)
          .append(" :idAfter)) ");
    }



    // 정렬
    jpql.append("ORDER BY ")
        .append(field)
        .append(" ")
        .append(direction)
        .append(", e.id ")
        .append(direction);

    TypedQuery<Employee> query =
        em.createQuery(
            jpql.toString(),
            Employee.class
        );



    if (StringUtils.hasText(request.keyword())) {

      query.setParameter(
          "keyword",
          "%" + request.keyword() + "%"
      );
    }

    if (request.status() != null) {

      query.setParameter(
          "status",
          request.status()
      );
    }

    if (StringUtils.hasText(request.employeeNumber())) {

      query.setParameter(
          "employeeNumber",
          request.employeeNumber()
      );
    }

    if (StringUtils.hasText(request.departmentName())) {

      query.setParameter(
          "departmentName",
          request.departmentName()
      );
    }

    if (StringUtils.hasText(request.position())) {

      query.setParameter(
          "position",
          request.position()
      );
    }

    if (request.hireDateFrom() != null) {

      query.setParameter(
          "hireDateFrom",
          request.hireDateFrom()
              .atStartOfDay(ZoneOffset.UTC)
              .toInstant()
      );
    }

    if (request.hireDateTo() != null) {

      query.setParameter(
          "hireDateTo",
          request.hireDateTo()
              .plusDays(1)
              .atStartOfDay(ZoneOffset.UTC)
              .toInstant()
      );
    }



    if (hasCursor) {
      if ("hireDate"
          .equalsIgnoreCase(request.sortField())) {
        query.setParameter(
            "cursor",
            Instant.parse(request.cursor())
        );
      } else {
        query.setParameter(
            "cursor",
            request.cursor()
        );
      }
      query.setParameter(
          "idAfter",
          request.idAfter()
      );
    }

    query.setMaxResults(request.size() + 1);

    return query.getResultList();
  }


  @Override
  public long countEmployees() {
    return em.createQuery(
            "SELECT COUNT(e) FROM Employee e WHERE e.deletedAt IS NULL",
            Long.class
        )
        .getSingleResult();
  }


}