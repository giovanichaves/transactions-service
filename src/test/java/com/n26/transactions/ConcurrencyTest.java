package com.n26.transactions;

import com.n26.statistics.StatisticsService;
import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcurrencyTest extends MultithreadedTestCase {

    private TransactionsService transactionsService;
    private StatisticsService statisticsService;

    private UTCTimeProvider timeProvider = new UTCTimeProvider();
    private Random random = new Random();

    @Override
    public void initialize() {
        var transactionsRepository = new TransactionsRepository(new MinuteBucketsProvider(), this.timeProvider);
        this.transactionsService = new TransactionsService(transactionsRepository, this.timeProvider);
        this.statisticsService = new StatisticsService(transactionsRepository);
    }

    @Override
    public void finish() {
        assertThat(this.statisticsService.retrieveStatistics().getCount(), equalTo(10L));
    }

    public void thread1() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread2() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread3() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread4() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread5() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread6() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread7() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread8() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread9() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }
    public void thread10() {
        this.transactionsService.registerTransaction(
                BigDecimal.TEN,
                this.timeProvider.now().minusSeconds(this.random.nextInt(59))
        );
    }

    @Test
    @DisplayName("Several threads concurrently registering transactions should give the correct count")
    void concurrentRegisterTransaction() throws Throwable {
        TestFramework.runManyTimes(new ConcurrencyTest(), 2000);
    }
}
