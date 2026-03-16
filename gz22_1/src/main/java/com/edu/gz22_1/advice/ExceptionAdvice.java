package com.edu.gz22_1.advice;

import com.edu.gz22_1.dto.ProblemDetail;
import com.edu.gz22_1.exception.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return createResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage());
    }

    private ResponseEntity<ProblemDetail> createResponse(HttpStatus status, String title,
            String detail) {
        ProblemDetail problem = ProblemDetail.builder()
                .title(title)
                .detail(detail)
                .build();
        problem.setStatus(status.value());
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

}
