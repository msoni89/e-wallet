package org.project.controllers;

import lombok.AllArgsConstructor;
import org.project.dtos.CreateWallet;
import org.project.dtos.wallet.WalletDTO;
import org.project.services.IWalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WalletRestController {

    private final IWalletService walletService;

    @GetMapping("/{userId}/list")
    public ResponseEntity<List<WalletDTO>> getAllWalletById(
            @PathVariable("userId") Long id) {
        return ResponseEntity.ok().body(walletService.listUserWallets(id));
    }

    // for external transfer/ so anyone can transfer other wallet (beneficiary)
    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<WalletDTO> getWalletByAccountNumber(
            @PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok().body(walletService.getWalletByAccountNumber(accountNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> getWalletById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(walletService.getWalletById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<WalletDTO> createWallet(
            @PathVariable("userId") Long userId, @RequestBody CreateWallet createWallet) {
        return ResponseEntity.ok().body(walletService.create(userId, createWallet));
    }

    @PostMapping("/{id}/debit")
    public ResponseEntity<Void> debitAmount(
            @PathVariable("id") Long id,
            @RequestParam(name = "amount", defaultValue = "0.00", required = true) BigDecimal amount) {
        walletService.debitAmount(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer/{from}/{to}")
    public ResponseEntity<Void> transferAmount(
            @PathVariable("from") Long fromWalletId,
            @PathVariable("to") Long toWalletId,
            @RequestParam(name = "amount", defaultValue = "0.00", required = true) BigDecimal amount) {
        walletService.transfer(fromWalletId, toWalletId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/credit")
    public ResponseEntity<Void> creditAmount(
            @PathVariable("id") Long id,
            @RequestParam(name = "amount", defaultValue = "0.00", required = true) BigDecimal amount) {
        walletService.creditAmount(id, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<WalletDTO> enabledWallet(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(walletService.enable(id));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<WalletDTO> disableWallet(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(walletService.disable(id));
    }

}
