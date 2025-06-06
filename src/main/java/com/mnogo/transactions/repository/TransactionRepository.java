package com.mnogo.transactions.repository;

import com.mnogo.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = """
        SELECT t.*
        FROM transactions t
        LEFT JOIN accounts a1 ON t.from_account_id = a1.account_id
        LEFT JOIN accounts a2 ON t.to_account_id = a2.account_id
        LEFT JOIN users u ON (a1.user_id = u.user_id OR a2.user_id = u.user_id)
        WHERE u.user_id = :userId
        ORDER BY t.transaction_datetime DESC
        """, nativeQuery = true)
    List<Transaction> findAllTransactionsByUserId(@Param("userId") long userId);
}
