package com.n26.transactions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionsRepositoryTest {

    private TransactionsRepository transactionsRepository;

    @Mock
    private UTCTimeProvider timeProvider;

    @Mock
    private MinuteBucketsProvider bucketsProvider;

    private ConcurrentHashMap<Integer, TransactionBucket> lastMinuteBuckets;

    private ZonedDateTime transactionTimeAt(int minute, int second) {
        return ZonedDateTime.of(2020,3,29,20,minute,second,0, ZoneOffset.UTC);
    }

    @Test
    @DisplayName("Cleans expired buckets older than 1 minute")
    void cleansExpiredBuckets() {
        this.lastMinuteBuckets = new MinuteBucketsProvider().createBuckets();

        when(this.bucketsProvider.createBuckets()).thenReturn(this.lastMinuteBuckets);
        when(this.timeProvider.now()).thenReturn(transactionTimeAt(4, 18));

        this.transactionsRepository = new TransactionsRepository(this.bucketsProvider, this.timeProvider);

        // <- 3:19 already clean
        // 3:20 -> minute stored <- 4:19

        // 4:19 last tx
        //  ------------------
        //  |    <stored>    |
        // 3:20             4:19

        // 5:13 new tx
        // 3:20 -> reset <- 4:13
        // 4:14 -> keep <- 5:13

        //3:20                4:19
        // |                   |
        // ---------------------
        // ***********----------+++++++++++++++
        //    <reset> | <keep> |   <new txs>  |
        //           4:14                    5:13

        // 3:00+
        this.lastMinuteBuckets.compute(23, (i, bucket) -> bucket.addTransaction(BigDecimal.valueOf(35.22)));
        this.lastMinuteBuckets.compute(34, (i, bucket) -> bucket
                .addTransaction(BigDecimal.valueOf(88.99))
                .addTransaction(BigDecimal.valueOf(1.00))
                .addTransaction(BigDecimal.valueOf(1.777))
        );

        // 4:00+
        this.lastMinuteBuckets.compute(7, (i, bucket) -> bucket.addTransaction(BigDecimal.ONE));
        this.lastMinuteBuckets.compute(12, (i, bucket) -> bucket.addTransaction(BigDecimal.TEN));
        this.lastMinuteBuckets.compute(14, (i, bucket) -> bucket.addTransaction(BigDecimal.TEN));
        this.lastMinuteBuckets.compute(17, (i, bucket) -> bucket.addTransaction(BigDecimal.TEN));
        this.lastMinuteBuckets.compute(18, (i, bucket) -> bucket.addTransaction(BigDecimal.TEN));

        when(this.timeProvider.now()).thenReturn(transactionTimeAt(5, 13));
        // trigger clean-up after 55 seconds of inactivity
        this.transactionsRepository.getLastMinuteBuckets();

        // 5:13
        this.lastMinuteBuckets.compute(13, (i, bucket) -> bucket.addTransaction(BigDecimal.TEN));

        // 3:00+
        assertThat(this.lastMinuteBuckets.get(23).getCount(), equalTo(0L));
        assertThat(this.lastMinuteBuckets.get(34).getCount(), equalTo(0L));

        // 4:00+
        assertThat(this.lastMinuteBuckets.get(7).getCount(), equalTo(0L));
        assertThat(this.lastMinuteBuckets.get(12).getCount(), equalTo(0L));
        assertThat(this.lastMinuteBuckets.get(14).getCount(), equalTo(1L));
        assertThat(this.lastMinuteBuckets.get(17).getCount(), equalTo(1L));
        assertThat(this.lastMinuteBuckets.get(18).getCount(), equalTo(1L));

        // 5:13
        assertThat(this.lastMinuteBuckets.get(13).getCount(), equalTo(1L));
    }

}
