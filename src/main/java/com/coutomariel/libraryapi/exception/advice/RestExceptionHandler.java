package com.coutomariel.libraryapi.exception.advice;

import com.coutomariel.libraryapi.exception.BussinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(MethodArgumentNotValidException ex){
        List<ErrorObject> errors = getErrors(ex);
        ErrorResponse errorResponse = getErrorResponse(ex, HttpStatus.BAD_REQUEST, errors);
        return errorResponse;
    }

    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handle(BussinessException ex){
        List<ErrorObject> errors = Arrays.asList(new ErrorObject(ex.getMessage(), "isbn", null));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(422)
                .message(ex.getMessage())
                .objectName("BookDto")
                .status(HttpStatus.UNPROCESSABLE_ENTITY.toString())
                .errors(errors)
                .build();
        return errorResponse;
    }

    private ErrorResponse getErrorResponse(MethodArgumentNotValidException ex, HttpStatus status, List<ErrorObject> errors) {
        return new ErrorResponse("Requisição possui campos inválidos", status.value(),
                status.getReasonPhrase(), ex.getBindingResult().getObjectName(), errors);
    }

    private List<ErrorObject> getErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorObject(error.getDefaultMessage(), error.getField(), error.getRejectedValue()))
                .collect(Collectors.toList());
    }
}