package com.eteration.simplebanking;


import com.eteration.simplebanking.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTest {

    @Test
    public void testCreateAccountAndSetBalance0() {
        Account account = new Account("Kerem Karaca", "17892");
        assertTrue(account.getOwner().equals("Kerem Karaca"));
        assertTrue(account.getAccountNumber().equals("17892"));
        assertTrue(account.getBalance() == 0);
    }

    @Test
    public void testDepositIntoBankAccount() {
        Account account = new Account("Demet Demircan", "9834");
        account.deposit(100);
        assertTrue(account.getBalance() == 100);
    }

    @Test
    public void testWithdrawFromBankAccount() throws InsufficientBalanceException {
        Account account = new Account("Demet Demircan", "9834");
        account.deposit(100);
        assertTrue(account.getBalance() == 100);
        account.withdraw(50);
        assertTrue(account.getBalance() == 50);
    }

    @Test
    public void testWithdrawException() {
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            Account account = new Account("Demet Demircan", "9834");
            account.deposit(100);
            account.withdraw(500);
        });

    }

    @Test
    public void testTransactions() throws InsufficientBalanceException {
        // Create account
        Account account = new Account("Canan Kaya", "1234");
        assertTrue(account.getTransactions().size() == 0);

        // Deposit Transaction
        DepositTransaction depositTrx = new DepositTransaction(100);
        assertTrue(depositTrx.getDate() != null);
        account.post(depositTrx);
        assertTrue(account.getBalance() == 100);
        assertTrue(account.getTransactions().size() == 1);

        // Withdrawal Transaction
        WithdrawalTransaction withdrawalTrx = new WithdrawalTransaction(60);
        assertTrue(withdrawalTrx.getDate() != null);
        account.post(withdrawalTrx);
        assertTrue(account.getBalance() == 40);
        assertTrue(account.getTransactions().size() == 2);

        // Bill Payment Transaction
        BillPaymentTransaction billPaymentTrx = new BillPaymentTransaction("Vodafone", "5423345566", 10.50);
        assertTrue(billPaymentTrx.getDate() != null);
        account.post(billPaymentTrx);
        assertTrue(account.getBalance() == 29.50);
        assertTrue(account.getTransactions().size() == 3);
    }
}
