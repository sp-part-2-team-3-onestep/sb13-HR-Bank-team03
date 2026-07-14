package com.project.hrbank.exception;

public class EmployeeNotExistException extends BaseException {

    public EmployeeNotExistException(String message, String detail) {
        super(message, detail);
    }
}
