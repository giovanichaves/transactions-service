package com.n26.transactions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping("/transactions")
    public ResponseEntity postTransactions(@RequestBody TransactionDTO transactionDTO) {

        transactionsService.registerTransaction(transactionDTO.getAmount(), transactionDTO.getTimestamp());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/transactions")
    public ResponseEntity deleteTransactions() {

        transactionsService.deleteTransactions();

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
