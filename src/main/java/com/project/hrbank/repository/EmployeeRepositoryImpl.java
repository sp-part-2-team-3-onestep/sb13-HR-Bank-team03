package com.project.hrbank.repository;

import com.project.hrbank.domain.Employee;
import com.project.hrbank.dto.request.EmployeeSearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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