package com.project.hrbank.exception;

public class EmployeeDuplicateException extends BaseException {
    public EmployeeDuplicateException(String message, String detail) {
        super(message,detail);
    }
}