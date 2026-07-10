package com.project.hrbank.exception;

public class DepartmentNotExistException extends BaseException {
    public DepartmentNotExistException(String message,String detail) {
        super(message,detail);
    }
}
