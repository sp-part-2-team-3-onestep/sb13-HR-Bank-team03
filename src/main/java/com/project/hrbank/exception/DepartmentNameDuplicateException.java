package com.project.hrbank.exception;

public class DepartmentNameDuplicateException extends BaseException {
    public DepartmentNameDuplicateException(String message, String detail) {
        super(message,detail);
    }
}