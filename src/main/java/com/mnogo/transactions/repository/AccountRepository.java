package com.mnogo.transactions.repository;

import com.mnogo.transactions.entity.Account;
import com.mnogo.transactions.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByOwner_Id(long userId);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM Account a
                WHERE a.owner.id = :userId AND a.type = :type AND a.balance < :threshold
            """)
    boolean existsCreditAccountWithLowBalance(@Param("userId") Long userId,
                                              @Param("type") AccountType type,
                                              @Param("threshold") float threshold);

    default boolean existsCreditAccountWithLowBalance(Long userId, float threshold) {
        return existsCreditAccountWithLowBalance(userId, AccountType.CREDIT, threshold);
    }
}
