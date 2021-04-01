package com.n26.statistics;

import com.n26.transactions.TransactionBucket;
import com.n26.transactions.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class StatisticsService {

    private final TransactionsRepository transactionsRepository;

    public StatisticsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public StatisticsDTO retrieveStatistics() {
        var sum = BigDecimal.ZERO;
        var avg = BigDecimal.ZERO;
        var max = BigDecimal.ZERO;
        BigDecimal min = null;
        var count = 0L;

        synchronized (this.transactionsRepository) {
            for (TransactionBucket bucket : this.transactionsRepository.getLastMinuteBuckets().values()) {

                sum = sum.add(bucket.getSum());

                if (min == null) {
                    min = bucket.getMin();
                } else if (bucket.getMin() != null) {
                    min = min.min(bucket.getMin());
                }

                max = max.max(bucket.getMax());

                count += bucket.getCount();
            }
        }

        if (min == null) {
            min = BigDecimal.ZERO;
        }

        if (count > 0) {
            avg = sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        }

        return new StatisticsDTO(
                sum.setScale(2, RoundingMode.HALF_UP).toString(),
                avg.setScale(2, RoundingMode.HALF_UP).toString(),
                max.setScale(2, RoundingMode.HALF_UP).toString(),
                min.setScale(2, RoundingMode.HALF_UP).toString(),
                count
        );
    }
}
