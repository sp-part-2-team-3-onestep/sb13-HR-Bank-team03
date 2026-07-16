package com.project.hrbank.domain;


public enum BackupStatus {
    RUNNING("IN_PROGRESS"),
    DONE("COMPLETED"),
    FAIL("FAILED"),
    SKIP("SKIPPED");

    private final String status;

    BackupStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return status;
    }
}