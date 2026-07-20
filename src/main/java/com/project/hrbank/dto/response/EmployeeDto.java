package com.project.hrbank.dto.response;

public record EmployeeDto(
    Long id,
    String name,
    String email,
    String employeeNumber,
    Long departmentId,
    String departmentName,
    String position,
    String hireDate,   // 임시. 엔티티 저장타입을 String 으로 변환하면 쉽게 변환
    String status,
    Long profileImageId
) {
}