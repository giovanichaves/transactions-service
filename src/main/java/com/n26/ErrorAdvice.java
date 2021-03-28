package com.n26;

import com.n26.transactions.TransactionInTheFutureException;
import com.n26.transactions.TransactionTooOldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TransactionInTheFutureException.class)
    ResponseEntity transactionInTheFutureError() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @ExceptionHandler(TransactionTooOldException.class)
    ResponseEntity transactionTooOldError() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
