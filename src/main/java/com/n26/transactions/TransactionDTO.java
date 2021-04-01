package com.n26.transactions;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class TransactionDTO {
    private final BigDecimal amount;
    private final ZonedDateTime timestamp;
}
