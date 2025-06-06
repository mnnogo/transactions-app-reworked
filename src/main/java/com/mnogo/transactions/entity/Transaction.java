package com.mnogo.transactions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Transaction {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(cascade = {})  // чтобы помнить что такое есть
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne(cascade = {})
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @Column(name = "transaction_datetime", nullable = false)
    private LocalDateTime transactionDateTime;

    @Column(name = "sum", nullable = false)
    private float sum;
}
