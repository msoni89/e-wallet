package org.project.repositories;

import org.project.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {


    // TODO Somehow OR query not working here...
    @Query("SELECT t FROM Transaction t WHERE t.fromWallet.user.id = :id")
    List<Transaction> findUserFromTransactions(@Param("id") Long id);

    @Query("SELECT t FROM Transaction t WHERE t.toWallet.user.id = :id")
    List<Transaction> findUserToTransactions(@Param("id") Long id);
}
