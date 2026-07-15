package com.project.hrbank.exception;

public class FileNotExistException extends BaseException {

    public FileNotExistException(String message, String detail) {
        super(message, detail);
    }
}