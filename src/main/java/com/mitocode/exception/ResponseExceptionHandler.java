package com.mitocode.exception;

import org.springframework.http.*;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.LocalDate;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleAllException(ModelNotFoundException exception, WebRequest request){
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(LocalDate.now(), exception.getMessage(), request.getDescription(false) );

        return new ResponseEntity<>(customErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleModelNotFoundException(ModelNotFoundException exception, WebRequest request){
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(LocalDate.now(), exception.getMessage(), request.getDescription(false) );

        return new ResponseEntity<>(customErrorResponse, HttpStatus.NOT_FOUND);
    }

    // Desde Spring Bot 3
    /*@ExceptionHandler(ModelNotFoundException.class)
    public ProblemDetail handleModelNotFoundException(ModelNotFoundException exception, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,exception.getMessage());
        problemDetail.setTitle("Mot Not Found Exception");
        problemDetail.setType(URI.create(request.getDescription(false)));

        return problemDetail;
    }*/
    /*@ExceptionHandler(ModelNotFoundException.class)
    public ErrorResponse handleModelNotFoundException(ModelNotFoundException exception, WebRequest request){
        return  ErrorResponse.builder(exception,HttpStatus.NOT_FOUND, exception.getMessage()).build();
    }*/

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {


        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField().concat(": ").concat(e.getDefaultMessage())
                ).collect(Collectors.joining());

        CustomErrorResponse customErrorResponse = new CustomErrorResponse(LocalDate.now(), msg, request.getDescription(false) );

        return new ResponseEntity<>(customErrorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
