package com.n26.transactions;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Service
public class TransactionsService {

    public void registerTransaction(BigDecimal amount, ZonedDateTime timestamp) {
        if (timestamp.isBefore(ZonedDateTime.now().minusMinutes(1))) {
            throw new TransactionTooOldException(amount, timestamp);
        }
    }
}
