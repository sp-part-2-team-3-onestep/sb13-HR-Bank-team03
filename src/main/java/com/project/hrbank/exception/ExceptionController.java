package com.project.hrbank.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionController {

    /**
     * ExceptionHandler 가 비어있으면 에러 납니다.
     * 에러를 추가 할 때, 주석을 풀어서 사용 해 주세요!
     */


    // status code - 400 error
    @ExceptionHandler({
        DepartmentNameDuplicateException.class
    })
    public ProblemDetail BadRequestException(BaseException e, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(BAD_REQUEST, e.getMessage());
        pd.setProperty("message", e.getMessage());
        pd.setProperty("timestamp", e.timestamp().toString());
        return pd;
    }


    // status code - 404 error
    @ExceptionHandler({
        DepartmentNotExistException.class
    })
    public ProblemDetail NotFoundException(BaseException e, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(NOT_FOUND, e.getMessage());
        pd.setProperty("message", e.getMessage());
        pd.setProperty("timestamp", e.timestamp().toString());
        return pd;
    }
//
//    // status code - 409 error
//    @ExceptionHandler({
//
//    })
//    public ProblemDetail ConflictException(BaseException e, WebRequest request) {
//        ProblemDetail pd = ProblemDetail.forStatusAndDetail(CONFLICT, e.getMessage());
//        pd.setProperty("message", e.getMessage());
//        pd.setProperty("timestamp", e.timestamp().toString());
//        return pd;
//    }

    // 500 error (default)
    @ExceptionHandler({
            BaseException.class,
    })
    public ProblemDetail InternalException(BaseException e, WebRequest request) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, e.detail());
        pd.setProperty("message", e.getMessage());
        pd.setProperty("timestamp", e.timestamp().toString());
        return pd;
    }

}
