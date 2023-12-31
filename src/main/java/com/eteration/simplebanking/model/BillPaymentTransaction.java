package com.eteration.simplebanking.model;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@DiscriminatorValue("BillPaymentTransaction")
public class BillPaymentTransaction extends Transaction {
    @Column(name = "payee", nullable = true)
    private String payee;

    @Column(name = "phoneNumber", nullable = true)
    private String phoneNumber;

    public BillPaymentTransaction(String payee, String phoneNumber, double amount) {
        super(amount);
        this.payee = payee;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void process(Account account) throws InsufficientBalanceException {
        account.withdraw(this.getAmount());
    }
}
