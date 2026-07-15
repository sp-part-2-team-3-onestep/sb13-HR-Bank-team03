package com.project.hrbank.domain;


public enum BackupStatus {
    RUNNING("run"),
    DONE("done"),
    FAIL("fail"),
    SKIP("skip");

    private final String status;

    BackupStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}