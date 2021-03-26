package com.n26.transactions;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MinuteBucketsProvider {

    public ConcurrentHashMap<Integer, TransactionBucket> createBuckets() {
        ConcurrentHashMap<Integer, TransactionBucket> minuteBuckets = new ConcurrentHashMap<>();
        for (int second = 0; second < 60; second++) {
            minuteBuckets.put(second, new TransactionBucket());
        }
        return minuteBuckets;
    }

}
