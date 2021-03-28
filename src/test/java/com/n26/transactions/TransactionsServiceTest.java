package com.n26.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {

    private TransactionsService transactionsService;

    @Mock
    private TransactionsRepository transactionsRepository;

    @Mock
    private ConcurrentHashMap<Integer, TransactionBucket> buckets;

    @BeforeEach
    void setUp() {
        transactionsService = new TransactionsService(transactionsRepository);
    }

    @Test
    @DisplayName("Throw exception if transaction timestamp is too old")
    void failsWhenTransactionTooOld() {
        Assertions.assertThrows(TransactionTooOldException.class, () -> {
            transactionsService.registerTransaction(BigDecimal.TEN, ZonedDateTime.now().minusMinutes(2));
        });
    }

    @Test
    @DisplayName("Throw exception if transaction timestamp is in the future")
    void failsWhenTransactionInTheFuture() {
        Assertions.assertThrows(TransactionInTheFutureException.class, () -> {
            transactionsService.registerTransaction(BigDecimal.TEN, ZonedDateTime.now().plusSeconds(2));
        });
    }

    @Test
    @DisplayName("Adds the transaction amount to the correct bucket")
    void addsTransactionToCorrectBucket() {
        when(transactionsRepository.getLastMinuteBuckets()).thenReturn(buckets);

        var now = ZonedDateTime.now();
        var transactionTimestamp = now.minusSeconds(5);
        transactionsService.registerTransaction(BigDecimal.TEN, transactionTimestamp);
        var bifunctionCaptor = ArgumentCaptor.forClass(BiFunction.class);
        var bucket = mock(TransactionBucket.class);

        verify(transactionsRepository.getLastMinuteBuckets()).compute(eq(transactionTimestamp.getSecond()), bifunctionCaptor.capture());

        bifunctionCaptor.getValue().apply(null, bucket);
        verify(bucket).addTransaction(BigDecimal.TEN);
    }

    @Test
    @DisplayName("Deletes all transactions from repository")
    void deletesAllTransactions() {
        when(transactionsRepository.getLastMinuteBuckets()).thenReturn(buckets);

        transactionsService.deleteTransactions();
        var biconsumerCaptor = ArgumentCaptor.forClass(BiConsumer.class);
        var bucket = mock(TransactionBucket.class);

        verify(transactionsRepository.getLastMinuteBuckets()).forEach(biconsumerCaptor.capture());

        biconsumerCaptor.getValue().accept(null, bucket);
        verify(bucket).resetBucket();
    }
}