package com.project.hrbank.exception;

import java.time.Instant;

public class BaseException extends RuntimeException {
    private final Instant timestamp;
    private final String detail;

    public BaseException(String message,String detail) {
        super(message);
        this.timestamp  = Instant.now();
        this.detail = detail;
    }

    public BaseException(String message){
        super(message);
        this.timestamp  = Instant.now();
        this.detail = message;
    }

    public BaseException(){
        super("");
        this.timestamp  = Instant.now();
        this.detail = "";
    }

    public Instant timestamp() {
        return timestamp;
    }

    public String detail() {
        return detail;
    }

}