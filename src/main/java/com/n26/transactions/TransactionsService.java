package com.n26.transactions;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;

    public TransactionsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public void registerTransaction(BigDecimal amount, ZonedDateTime timestamp) {
        if (timestamp.isBefore(ZonedDateTime.now().minusMinutes(1))) {
            throw new TransactionTooOldException(amount, timestamp);
        }

        if (timestamp.isAfter(ZonedDateTime.now())) {
            throw new TransactionInTheFutureException(amount, timestamp);
        }

        var bucketNum = timestamp.getSecond();

        synchronized (this) {
            transactionsRepository.getLastMinuteBuckets().compute(bucketNum, (i, secondBucket) -> secondBucket.addTransaction(amount));
        }
    }

    public void deleteTransactions() {
        synchronized (this) {
            transactionsRepository.getLastMinuteBuckets().forEach((integer, transactionBucket) -> transactionBucket.resetBucket());
        }
    }
}
