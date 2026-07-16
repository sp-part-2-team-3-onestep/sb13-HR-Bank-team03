package com.project.hrbank.domain;

public enum EmployeeHistoryType {

    CREATED("CREATED"),
    UPDATED("UPDATED"),
    DELETED("DELETED");


    private final String status;

    EmployeeHistoryType(String status) {
        this.status = status;
    }

    public String toString() {
        return status;
    }

}