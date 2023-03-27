package org.project.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "WALLETS_TBL")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(updatable = false)
    Timestamp createdAt;
    @UpdateTimestamp
    Timestamp updatedAt;
    private String name;

    private String accountNumber;
    // TODO Generate based on pattern BNK-0000012

    @OneToOne
    @JoinColumn(name = "currency_fk", referencedColumnName = "id")
    private Currency currency;

    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "fromWallet", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transaction> transactions;

}
