package com.n26.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {

    private TransactionsService transactionsService;

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private ConcurrentHashMap<Integer, TransactionBucket> buckets;

    @BeforeEach
    void setUp() {
        when(transactionsRepository.getLastMinuteBuckets()).thenReturn(buckets);

        transactionsService = new TransactionsService(transactionsRepository);
    }

    @Test
    @DisplayName("Throw exception is transaction timestamp is too old")
    void testFailsWhenTransactionTooOld() {
        transactionsService.registerTransaction(BigDecimal.TEN, ZonedDateTime.now().minusSeconds(2));

        Assertions.assertThrows(TransactionTooOldException.class, () -> {
            transactionsService.registerTransaction(BigDecimal.TEN, ZonedDateTime.now().minusMinutes(2));
        });
    }

    @Test
    @DisplayName("Adds the transaction amount to the correct bucket")
    void testAddsTransactionToCorrectBucket() {
        var now = ZonedDateTime.now();
        var transactionTimestamp = now.minusSeconds(5);
        transactionsService.registerTransaction(BigDecimal.TEN, transactionTimestamp);

        verify(transactionsRepository.getLastMinuteBuckets()).compute(eq(transactionTimestamp.getSecond()), any());
    }
}