package com.n26;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.transactions.TransactionInTheFutureException;
import com.n26.transactions.TransactionTooOldException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(TransactionInTheFutureException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    void transactionInTheFutureError() {}

    @ExceptionHandler(TransactionTooOldException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void transactionTooOldError() {}

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    void invalidFormatException() {}

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void httpMessageNotReadableException() {}

}
