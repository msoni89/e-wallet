package org.project.repositories;

import org.project.dtos.wallet.WalletDTO;
import org.project.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IWalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUser_Id(Long userId);

    Optional<Wallet> findByAccountNumber(String accountNumber);
}
