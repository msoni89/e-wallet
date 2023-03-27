package org.project.services.impl;

import lombok.AllArgsConstructor;
import org.project.dtos.CurrencyDTO;
import org.project.dtos.wallet.TransactionDTO;
import org.project.dtos.wallet.WalletDTO;
import org.project.models.Transaction;
import org.project.repositories.TransactionRepository;
import org.project.services.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionDTO> listUserWalletTransactions(Long id) {

        // TODO Need to change it use query

        return  Stream.concat(
                transactionRepository.findUserToTransactions(id).stream(),
                transactionRepository.findUserFromTransactions(id).stream()
        ).map(transaction ->
                TransactionDTO.builder()
                        .toWallet(transaction.getToWallet() != null ? WalletDTO.builder()
                                .balance(transaction.getToWallet().getBalance())
                                .user(transaction.getToWallet().getUser())
                                .accountNumber(transaction.getToWallet() != null ? transaction.getToWallet().getAccountNumber() : "")
                                .name(transaction.getToWallet().getName())
                                .currency(CurrencyDTO.builder()
                                        .code(transaction.getToWallet().getCurrency().getCode())
                                        .name(transaction.getToWallet().getCurrency().getName())
                                        .symbol(transaction.getToWallet().getCurrency().getSymbol()).build())
                                .build() : null)
                        .transactionType(transaction.getTransactionType())
                        .amount(transaction.getAmount())
                        .updatedAt(transaction.getUpdatedAt())
                        .fromWallet(transaction.getFromWallet() != null ? WalletDTO.builder()
                                .balance(transaction.getFromWallet().getBalance())
                                .user(transaction.getFromWallet().getUser())
                                .accountNumber(transaction.getFromWallet() != null ? transaction.getFromWallet().getAccountNumber(): null)
                                .name(transaction.getFromWallet().getName())
                                .currency(CurrencyDTO.builder()
                                        .code(transaction.getFromWallet().getCurrency().getCode())
                                        .name(transaction.getFromWallet().getCurrency().getName())
                                        .symbol(transaction.getFromWallet().getCurrency().getSymbol()).build())
                                .build() : null)
                        .build()
        ).toList();
    }
}
