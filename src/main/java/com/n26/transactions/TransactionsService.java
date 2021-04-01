package com.n26.transactions;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final UTCTimeProvider timeProvider;

    public TransactionsService(TransactionsRepository transactionsRepository, UTCTimeProvider timeProvider) {
        this.transactionsRepository = transactionsRepository;
        this.timeProvider = timeProvider;
    }

    public void registerTransaction(BigDecimal amount, ZonedDateTime timestamp) {
        var now = this.timeProvider.now();

        if (timestamp.isBefore(now.minusSeconds(59))) {
            throw new TransactionTooOldException(amount, timestamp);
        }

        if (timestamp.isAfter(now)) {
            throw new TransactionInTheFutureException(amount, timestamp);
        }

        var bucketNum = timestamp.getSecond();
        synchronized (this.transactionsRepository) {
            this.transactionsRepository.getLastMinuteBuckets().compute(bucketNum, (i, bucket) -> bucket.addTransaction(amount));
        }
    }

    public void deleteTransactions() {
        synchronized (this.transactionsRepository) {
            this.transactionsRepository.getLastMinuteBuckets().forEach((i, bucket) -> bucket.resetBucket());
        }
    }
}
