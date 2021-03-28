package com.n26.transactions;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class TransactionInTheFutureException extends RuntimeException {
    public TransactionInTheFutureException(BigDecimal amount, ZonedDateTime timestamp) {
        super(
                String.format(
                        "Transaction with amount %s from %s is in the future",
                        amount,
                        timestamp
                )
        );
    }
}
