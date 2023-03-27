package org.project.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.project.dtos.CreateWallet;
import org.project.dtos.CurrencyDTO;
import org.project.dtos.wallet.WalletDTO;
import org.project.enums.TransactionType;
import org.project.exceptions.NotFoundException;
import org.project.exceptions.TransactionFailed;
import org.project.models.Currency;
import org.project.models.Transaction;
import org.project.models.User;
import org.project.models.Wallet;
import org.project.repositories.CurrencyRepository;
import org.project.repositories.TransactionRepository;
import org.project.repositories.UserRepository;
import org.project.repositories.WalletRepository;
import org.project.utils.GenerateRandomAccountNumber;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class WalletService implements org.project.services.WalletService {

    private final Integer MIN_OPERATIONAL_AMOUNT = 0;

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final CurrencyRepository currencyRepository;

    /**
     * Debit amount
     * @param walletId
     * @param amount
     */
    @Override
    @Transactional
    public void debitAmount(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException(String.format("Wallet not found with id %d", walletId)));

        BigDecimal absAmount = amount.abs();

        if (absAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new TransactionFailed(String.format("Amount should be greater then min transfer allowed amount which is %d", MIN_OPERATIONAL_AMOUNT));

        BigDecimal predicatedBalance = wallet.getBalance().subtract(absAmount);

        if (predicatedBalance.compareTo(BigDecimal.ZERO) < 0) // okay, to drain whole amount.
            throw new TransactionFailed(String.format("Wallet ( %d) does not have sufficient balance", walletId));

        wallet.setUpdatedAt(Timestamp.from(Instant.now()));
        wallet.setBalance(predicatedBalance);

        var updated = walletRepository.save(wallet);

        Transaction transaction = Transaction.builder().amount(amount)
                .transactionType(TransactionType.DEBIT)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .fromWallet(updated).build();
        transactionRepository.save(transaction);
    }

    /**
     * Credit Wallet
     * @param walletId
     * @param amount
     */
    @Override
    @Transactional
    public void creditAmount(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException(String.format("Wallet not found with id %d", walletId)));

        // TODO check user not disabled

        BigDecimal absAmount = amount.abs();

        if (absAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new TransactionFailed(String.format("Amount should be greater then min transfer allowed amount which is %d", MIN_OPERATIONAL_AMOUNT));

        wallet.setUpdatedAt(Timestamp.from(Instant.now()));
        wallet.setBalance(wallet.getBalance().add(absAmount));

        var updated = walletRepository.save(wallet);

        Transaction transaction = Transaction.builder().amount(amount)
                .transactionType(TransactionType.CREDIT)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .fromWallet(updated).build();
        transactionRepository.save(transaction);
    }

    /**
     * Transfer - Method used for transfer between two different account (wallets). It will check user amount not below or equal to
     *
     * @param fromWalletId
     * @param toWalletId
     * @param amount
     */
    @Override
    @Transactional
    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        if (fromWalletId == toWalletId)
            throw new TransactionFailed("Transfer from/to should not be same");

        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new NotFoundException(String.format("From wallet does not exist with id %d", fromWalletId)));

        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new NotFoundException(String.format("To wallet does not exist with id %d", toWalletId)));

        // TODO handle cross currency amount check, do conversion and perform operation (UI should also handle it.)

        BigDecimal absAmount = amount.abs(); // Here we are protecting negative query parameters
        if (absAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new TransactionFailed(String.format("Amount should be greater then min transfer allowed amount which is %d", MIN_OPERATIONAL_AMOUNT));

        BigDecimal predicatedBalance = fromWallet.getBalance().subtract(absAmount);

        if (predicatedBalance.compareTo(BigDecimal.ZERO) < 0) // This is okay, if user want to drain complete account.
            throw new TransactionFailed(String.format("Sender Wallet ( %d) does not have sufficient balance", fromWalletId));

        fromWallet.setBalance(predicatedBalance);
        toWallet.setBalance(toWallet.getBalance().add(absAmount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        Transaction transaction = Transaction.builder().amount(amount)
                .transactionType(TransactionType.TRANSFER)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .fromWallet(fromWallet)
                .toWallet(toWallet).build();
        transactionRepository.save(transaction);
    }

    /**
     * Create wallet - this method check wallet exist or not, if ok() this will create wallet.
     *
     * @param userId
     * @param createWalletDTO
     * @return
     */
    @Override
    public WalletDTO create(Long userId, CreateWallet createWalletDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id %d", userId)));
        Currency currency = currencyRepository.findById(createWalletDTO.getCurrencyId())
                .orElseThrow(() -> new NotFoundException(String.format("Unsupported Currency id %d", userId)));
        Wallet wallet = Wallet.builder()
                .accountNumber(GenerateRandomAccountNumber.generateAccountNumber())
                .currency(currency)
                .name(createWalletDTO.getName())
                .user(user)
                .enabled(true)
                .createdAt(Timestamp.from(Instant.now()))
                .updatedAt(Timestamp.from(Instant.now()))
                .build();

        Wallet updated = walletRepository.save(wallet);

        return WalletDTO.builder()
                .enabled(updated.getEnabled())
                .id(updated.getId())
                .name(updated.getName())
                .updatedAt(updated.getUpdatedAt())
                .enabled(updated.getEnabled())
                .currency(CurrencyDTO.builder()
                        .id(wallet.getCurrency().getId())
                        .code(wallet.getCurrency().getCode())
                        .name(wallet.getCurrency().getName())
                        .symbol(wallet.getCurrency().getSymbol()).build())
                .accountNumber(updated.getAccountNumber())
                .balance(updated.getBalance())
                .accountNumber(updated.getAccountNumber())
                .build();
    }

    /**
     * Get Wallet by wallet id
     *
     * @param walletId
     * @return
     */
    @Override
    public WalletDTO getWalletById(Long walletId) {
        return walletRepository.findById(walletId).map(wallet -> WalletDTO.builder()
                        .enabled(wallet.getEnabled())
                        .id(wallet.getId())
                        .name(wallet.getName())
                        .enabled(wallet.getEnabled())

                        .currency(CurrencyDTO.builder()
                                .id(wallet.getCurrency().getId())
                                .code(wallet.getCurrency().getCode())
                                .name(wallet.getCurrency().getName())
                                .symbol(wallet.getCurrency().getSymbol()).build())
                        .updatedAt(wallet.getUpdatedAt())
                        .accountNumber(wallet.getAccountNumber())
                        .balance(wallet.getBalance())
                        .build())
                .orElseThrow(() -> new NotFoundException(String.format("Wallet not found with id %d", walletId)));
    }

    /**
     * Disable account
     *
     * @param walletId
     * @return
     */
    @Override
    public WalletDTO disable(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException(String.format("Wallet not found with id %d", walletId)));
        Wallet updated = walletRepository.save(wallet);
        return WalletDTO.builder()
                .enabled(updated.getEnabled())
                .id(updated.getId())
                .name(wallet.getName())
                .enabled(wallet.getEnabled())
                .updatedAt(updated.getUpdatedAt())
                .currency(CurrencyDTO.builder()
                        .id(wallet.getCurrency().getId())
                        .code(wallet.getCurrency().getCode())
                        .name(wallet.getCurrency().getName())
                        .symbol(wallet.getCurrency().getSymbol()).build())
                .accountNumber(updated.getAccountNumber())
                .balance(updated.getBalance())
                .accountNumber(updated.getAccountNumber())
                .build();
    }

    /**
     * Enable account
     *
     * @param walletId
     * @return
     */

    @Override
    public WalletDTO enable(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException(String.format("Wallet not found with id %d", walletId)));
        wallet.setEnabled(true);
        Wallet updated = walletRepository.save(wallet);
        return WalletDTO.builder()
                .enabled(updated.getEnabled())
                .id(updated.getId())
                .name(wallet.getName())
                .enabled(wallet.getEnabled())
                .updatedAt(updated.getUpdatedAt())
                .currency(CurrencyDTO.builder()
                        .id(wallet.getCurrency().getId())
                        .code(wallet.getCurrency().getCode())
                        .name(wallet.getCurrency().getName())
                        .symbol(wallet.getCurrency().getSymbol()).build())
                .accountNumber(updated.getAccountNumber())
                .balance(updated.getBalance())
                .accountNumber(updated.getAccountNumber())
                .build();
    }

    /**
     * List all user wallets
     * @param userId
     * @return
     */
    @Override
    public List<WalletDTO> listUserWallets(Long userId) {
        return walletRepository.findByUser_Id(userId).stream().map(wallet -> WalletDTO.builder()
                .enabled(wallet.getEnabled())
                .id(wallet.getId())
                .name(wallet.getName())
                .enabled(wallet.getEnabled())
                .updatedAt(wallet.getUpdatedAt())
                .currency(CurrencyDTO.builder()
                        .id(wallet.getCurrency().getId())
                        .code(wallet.getCurrency().getCode())
                        .name(wallet.getCurrency().getName())
                        .symbol(wallet.getCurrency().getSymbol()).build())
                .accountNumber(wallet.getAccountNumber())
                .balance(wallet.getBalance())
                .accountNumber(wallet.getAccountNumber())
                .build()).toList();
    }
}
