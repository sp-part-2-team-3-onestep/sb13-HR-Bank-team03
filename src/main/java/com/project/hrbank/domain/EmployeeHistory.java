package com.project.hrbank.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "employee_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeHistory extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeHistoryType type;

    @Column(name = "change_detail", columnDefinition = "TEXT")
    private String changeDetail;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
    public EmployeeHistory(
            Employee employee,
            Department department,
            EmployeeHistoryType type,
            String changeDetail,
            String memo,
            String ipAddress
    ) {
        this.employee = employee;
        this.department = department;
        this.type = type;
        this.changeDetail = changeDetail;
        this.memo = memo;
        this.ipAddress = ipAddress;
    }

    public Long getHistoryId() {
        return super.getId();
    }

    public Long getEmployeeId() {
        return employee.getId();
    }

    public String getEmployeeName() {
        return employee.getName();
    }

    public Long getDepartmentId() {
        return department.getId();
    }

    public String getDepartmentName() {
        return department.getDepartmentName();
    }

    public Instant getCreatedAt() {
        return super.getCreateAt();
    }
}