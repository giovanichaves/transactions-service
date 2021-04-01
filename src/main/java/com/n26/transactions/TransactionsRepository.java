package com.n26.transactions;

import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionsRepository {

    private final ConcurrentHashMap<Integer, TransactionBucket> lastMinuteBuckets;

    private ZonedDateTime lastBucketCleanUp;
    private final UTCTimeProvider timeProvider;

    public TransactionsRepository(MinuteBucketsProvider minuteBucketsProvider, UTCTimeProvider timeProvider) {
        this.lastMinuteBuckets = minuteBucketsProvider.createBuckets();
        this.lastBucketCleanUp = timeProvider.now();
        this.timeProvider = timeProvider;
    }

    public ConcurrentHashMap<Integer, TransactionBucket> getLastMinuteBuckets() {
        cleanExpiredBuckets();
        return lastMinuteBuckets;
    }

    private void cleanExpiredBuckets() {
        synchronized (this) {
            var now = this.timeProvider.now();

            if (this.lastBucketCleanUp.isBefore(now.minusMinutes(1))) {
                this.lastBucketCleanUp = now.minusMinutes(1);
            }

            while (this.lastBucketCleanUp.isBefore(now)) {
                this.lastBucketCleanUp = this.lastBucketCleanUp.plusSeconds(1);
                this.lastMinuteBuckets.get(this.lastBucketCleanUp.getSecond()).resetBucket();
            }
        }
    }
}
