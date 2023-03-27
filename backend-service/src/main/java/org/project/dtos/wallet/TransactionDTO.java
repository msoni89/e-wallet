package org.project.dtos.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.project.enums.TransactionType;
import org.project.models.Wallet;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class TransactionDTO {

    private Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    private BigDecimal amount;

    private TransactionType transactionType;

    private WalletDTO fromWallet;

    private WalletDTO toWallet;

}
