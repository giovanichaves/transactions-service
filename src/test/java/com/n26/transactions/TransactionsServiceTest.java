package com.n26.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

class TransactionsServiceTest {

    private TransactionsService transactionsService;

    @BeforeEach
    void setUp() {
        transactionsService = new TransactionsService();
    }

    @Test
    @DisplayName("Throw exception is transaction timestamp is too old")
    void failsWhenTransactionTooOld() {
        Assertions.assertThrows(TransactionTooOldException.class, () -> {
            transactionsService.registerTransaction(BigDecimal.TEN, ZonedDateTime.now().minusMinutes(2));
        });
    }
}