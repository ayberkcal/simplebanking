package com.eteration.simplebanking.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "balance", nullable = false)
    private double balance;

    @CreationTimestamp
    private LocalDateTime createdDate;


    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<Transaction> transactions = new HashSet<>();

    public Account(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
    }

    public void deposit(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException();

        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (balance < amount)
            throw new InsufficientBalanceException();
        this.balance -= amount;
    }

    public void post(Transaction transaction) throws InsufficientBalanceException {
        transaction.process(this);
        transactions.add(transaction);
    }
}
