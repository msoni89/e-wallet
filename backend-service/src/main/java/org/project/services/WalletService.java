package org.project.services;

import org.project.dtos.CreateWallet;
import org.project.dtos.wallet.WalletDTO;
import org.project.models.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    void debitAmount(Long walletId, BigDecimal amount);

    void creditAmount(Long walletId, BigDecimal amount);

    void transfer(Long  fromWalletId, Long  toWalletId, BigDecimal amount);

    WalletDTO create(Long userId, CreateWallet createWalletDTO);

    WalletDTO getWalletById(Long walletId);
    WalletDTO disable(Long walletId);
    WalletDTO enable(Long walletId);

    List<WalletDTO> listUserWallets(Long userId) ;

}
