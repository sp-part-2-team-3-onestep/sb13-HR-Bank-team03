package com.project.hrbank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

// 부서엔터티
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends Base {

    @Column(nullable = false, length = 20)
    private String departmentName;

    // 부서 설명란
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // 부서 설립일
    @Column(nullable = false)
    private LocalDate establishedDate;

    // 부서 삭제 날짜와 시간
    @Column
    private Instant deletedAt;

    public Department(
        String departmentName,
        String description,
        LocalDate establishedDate
    ){
        this.departmentName = departmentName;
        this.description = description;
        this.establishedDate = establishedDate;
    }

    public void delete() {
        this.deletedAt = Instant.now();
    }
}