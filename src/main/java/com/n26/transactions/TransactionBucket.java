package com.n26.transactions;

import java.math.BigDecimal;

public class TransactionBucket {

    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal max = BigDecimal.ZERO;
    private BigDecimal min = null;
    private long count = 0;

    public void addTransaction(BigDecimal amount) {
        synchronized (this) {
            this.sum = this.sum.add(amount);
            this.max = this.max.max(amount);
            this.min = this.min == null ? amount : this.min.min(amount);
            this.count++;
        }
    }

    public void resetBucket() {
        synchronized (this) {
            this.sum = BigDecimal.ZERO;
            this.max = BigDecimal.ZERO;
            this.min = null;
            this.count = 0;
        }
    }

}
