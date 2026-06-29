package com.badwallet.api.repository;

import com.badwallet.api.model.Transaction;
import com.badwallet.api.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletOrderByCreatedAtDesc(Wallet wallet);
}