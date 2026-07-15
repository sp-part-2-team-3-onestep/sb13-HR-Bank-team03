package com.project.hrbank.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Employee extends Base {

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false, unique = true)
    private String employeeNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private Instant hireDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "profile_imaged_id")
    private FileMeta profileImaged;

    private Instant deletedAt;

    public static Employee create(String name, Department department, String employeeNumber,
        String email, String position, Instant hireDate,
        EmployeeStatus status, FileMeta profileImaged) {
        Employee employee = new Employee();
        employee.name = name;
        employee.department = department;
        employee.employeeNumber = employeeNumber;
        employee.email = email;
        employee.position = position;
        employee.hireDate = hireDate;
        employee.status = status;
        employee.profileImaged = profileImaged;
        return employee;
    }

    public void update(String name, Department department, String email, String position,
        Instant hireDate, EmployeeStatus status, FileMeta profileImaged, Instant deletedAt) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.position = position;
        this.hireDate = hireDate;
        this.status = status;
        this.profileImaged = profileImaged;
        this.deletedAt = deletedAt;
    }
}