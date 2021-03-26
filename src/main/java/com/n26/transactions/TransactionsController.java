package com.n26.transactions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionsController {

    @PostMapping("/transactions")
    public ResponseEntity postTransactions(@RequestBody TransactionDTO transactionDTO) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
