package org.project.controllers;

import lombok.AllArgsConstructor;
import org.project.dtos.CreateWallet;
import org.project.dtos.wallet.WalletDTO;
import org.project.services.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wallet")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WalletRestController {

    private final WalletService walletService;

    @GetMapping("/{userId}/list")
    public ResponseEntity<List<WalletDTO>> getAllWalletById(
            @PathVariable("userId") Long id) {
        return ResponseEntity.ok().body(walletService.listUserWallets(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDTO> getWalletById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(walletService.getWalletById(id));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<WalletDTO> createWallet(
            @PathVariable("userId") Long userId, @RequestBody CreateWallet walletDTO) {
        return ResponseEntity.ok().body(walletService.create(userId, walletDTO));
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
