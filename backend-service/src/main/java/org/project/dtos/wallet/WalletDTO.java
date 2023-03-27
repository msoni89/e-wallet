package org.project.dtos.wallet;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.project.dtos.CurrencyDTO;
import org.project.models.Currency;
import org.project.models.Transaction;
import org.project.models.User;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor

public class WalletDTO {

    private Long id;
    Timestamp createdAt;
    Timestamp updatedAt;
    private String name;
    private String accountNumber;

    private CurrencyDTO currency;

    private BigDecimal balance = BigDecimal.ZERO;

    private Boolean enabled;

    private User user;

    private List<TransactionDTO> transactions;

}
