package org.project.services;

import org.project.dtos.wallet.TransactionDTO;

import java.util.List;

public interface TransactionService  {
    List<TransactionDTO> listUserWalletTransactions(Long id);
}
