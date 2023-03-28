package org.project.controllers;

import lombok.AllArgsConstructor;
import org.project.dtos.wallet.TransactionDTO;
import org.project.services.ITransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionRestController {

    private final ITransactionService transactionService;

    @GetMapping("/{userId}/list")
    public ResponseEntity<List<TransactionDTO>> listUserWalletTransactions(
            @PathVariable("userId") Long id) {
        return ResponseEntity.ok().body(transactionService.listUserWalletTransactions(id));
    }
}
