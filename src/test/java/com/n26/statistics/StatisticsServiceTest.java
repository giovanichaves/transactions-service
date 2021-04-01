package com.n26.statistics;

import com.n26.transactions.MinuteBucketsProvider;
import com.n26.transactions.TransactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @Mock
    private TransactionsRepository transactionsRepository;

    @BeforeEach
    void setUp() {
        var minuteBuckets = new MinuteBucketsProvider().createBuckets();
        minuteBuckets.get(1).addTransaction(BigDecimal.TEN);
        minuteBuckets.get(14).addTransaction(BigDecimal.valueOf(11.555));
        minuteBuckets.get(21).addTransaction(BigDecimal.valueOf(8.033));

        when(this.transactionsRepository.getLastMinuteBuckets()).thenReturn(minuteBuckets);

        this.statisticsService = new StatisticsService(this.transactionsRepository);
    }

    @Test
    @DisplayName("Retrieve sum, avg, min and max rounded half up with 2 decimals")
    void retrieveStatisticsValuesRoundedUpTwoDecimals() {

        var stats = this.statisticsService.retrieveStatistics();
        assertThat(stats.getSum(), equalTo("29.59"));
        assertThat(stats.getAvg(), equalTo("9.86"));
        assertThat(stats.getMax(), equalTo("11.56"));
        assertThat(stats.getMin(), equalTo("8.03"));
    }
}