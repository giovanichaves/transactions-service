package com.n26.transactions;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionsRepository {

    private final ConcurrentHashMap<Integer, TransactionBucket> lastMinuteBuckets;

    public TransactionsRepository(MinuteBucketsProvider minuteBucketsProvider) {
        lastMinuteBuckets = minuteBucketsProvider.createBuckets();
    }

    public ConcurrentHashMap<Integer, TransactionBucket> getLastMinuteBuckets() {
        return lastMinuteBuckets;
    }

}
