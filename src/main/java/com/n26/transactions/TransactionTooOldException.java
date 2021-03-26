package com.n26.transactions;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionTooOldException extends RuntimeException {

    public TransactionTooOldException(BigDecimal amount, ZonedDateTime timestamp) {
        super(
                String.format(
                        "Transaction with amount %s from %s is more than 60 seconds old",
                        amount,
                        timestamp
                )
        );
    }
}
