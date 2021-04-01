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

    private UTCTimeProvider timeProvider = new UTCTimeProvider();

    @BeforeEach
    void setUp() {
        this.transactionsService = new TransactionsService(this.transactionsRepository, this.timeProvider);
    }

    @Test
    @DisplayName("Throw exception if transaction timestamp is too old")
    void failsWhenTransactionTooOld() {
        Assertions.assertThrows(TransactionTooOldException.class, () -> {
            this.transactionsService.registerTransaction(BigDecimal.TEN, this.timeProvider.now().minusMinutes(2));
        });
    }

    @Test
    @DisplayName("Throw exception if transaction timestamp is in the future")
    void failsWhenTransactionInTheFuture() {
        Assertions.assertThrows(TransactionInTheFutureException.class, () -> {
            this.transactionsService.registerTransaction(BigDecimal.TEN, this.timeProvider.now().plusSeconds(2));
        });
    }

    @Test
    @DisplayName("Adds the transaction amount to the correct bucket")
    void addsTransactionToCorrectBucket() {
        when(this.transactionsRepository.getLastMinuteBuckets()).thenReturn(this.buckets);

        var transactionTimestamp = this.timeProvider.now().minusSeconds(5);
        this.transactionsService.registerTransaction(BigDecimal.TEN, transactionTimestamp);
        var bifunctionCaptor = ArgumentCaptor.forClass(BiFunction.class);
        var bucket = mock(TransactionBucket.class);

        verify(this.transactionsRepository.getLastMinuteBuckets()).compute(eq(transactionTimestamp.getSecond()), bifunctionCaptor.capture());

        bifunctionCaptor.getValue().apply(null, bucket);
        verify(bucket).addTransaction(BigDecimal.TEN);
    }

    @Test
    @DisplayName("Deletes all transactions from repository")
    void deletesAllTransactions() {
        when(this.transactionsRepository.getLastMinuteBuckets()).thenReturn(this.buckets);

        this.transactionsService.deleteTransactions();
        var biconsumerCaptor = ArgumentCaptor.forClass(BiConsumer.class);
        var bucket = mock(TransactionBucket.class);

        verify(this.transactionsRepository.getLastMinuteBuckets()).forEach(biconsumerCaptor.capture());

        biconsumerCaptor.getValue().accept(null, bucket);
        verify(bucket).resetBucket();
    }
}