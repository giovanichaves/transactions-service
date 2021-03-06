package com.n26.transactions;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransactionBucket {

    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal max = BigDecimal.ZERO;
    private BigDecimal min = null;
    private long count = 0;

    public TransactionBucket addTransaction(BigDecimal amount) {
        this.sum = this.sum.add(amount);
        this.max = this.max.max(amount);
        this.min = this.min == null ? amount : this.min.min(amount);
        this.count++;
        return this;
    }

    public void resetBucket() {
        this.sum = BigDecimal.ZERO;
        this.max = BigDecimal.ZERO;
        this.min = null;
        this.count = 0;
    }

}
