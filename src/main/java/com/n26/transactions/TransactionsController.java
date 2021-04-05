package com.n26.transactions;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionsController {

    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success!"),
            @ApiResponse(code = 204, message = "Transaction is older than 60 seconds"),
            @ApiResponse(code = 400, message = "JSON is invalid"),
            @ApiResponse(code = 422, message = "Fields are not parsable or the transaction date is in the future")
    })
    public void postTransactions(@RequestBody TransactionDTO transactionDTO) {
        transactionsService.registerTransaction(transactionDTO.getAmount(), transactionDTO.getTimestamp());
    }

    @DeleteMapping("/transactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransactions() {
        transactionsService.deleteTransactions();
    }
}
