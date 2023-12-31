package com.eteration.simplebanking.services;


import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.payloads.AccountResponse;
import com.eteration.simplebanking.payloads.AccountTransactionItem;
import com.eteration.simplebanking.repository.AccountRepository;
import com.eteration.simplebanking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The {@code AccountService} class provides functionalities for managing accounts and transactions.
 * It acts as a service layer between the controller and the data repositories.
 *
 * <p>The class includes methods for finding accounts, performing credit, debit, and bill payment transactions,
 * creating new accounts, and retrieving account details along with transaction history.
 *
 * <p>Dependencies:
 * - {@link AccountRepository}: Responsible for database operations related to accounts.
 * - {@link TransactionRepository}: Handles database operations for transactions.
 *
 * <p>Usage example:
 * <pre>
 * {@code
 *   AccountService accountService = new AccountService(accountRepository, transactionRepository);
 *   Account account = accountService.findAccount("123456789");
 *   accountService.credit(account, new DepositTransaction(100.0));
 *   AccountResponse accountResponse = accountService.getAccount(account);
 * }
 * </pre>
 *
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructs an {@code AccountService} with the specified repositories.
     *
     * @param accountRepository    Repository for account-related database operations.
     * @param transactionRepository Repository for transaction-related database operations.
     */
    @Autowired
    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Finds and returns an account based on the provided account number.
     *
     * @param accountNumber The account number to search for.
     * @return The found account.
     * @throws AccountNotFoundException If the account with the specified number is not found.
     */
    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException::new);
        return account;
    }

    /**
     * Credits the specified account with the provided deposit transaction.
     *
     * @param account      The account to credit.
     * @param transaction  The deposit transaction to perform.
     * @throws InsufficientBalanceException If the account balance is insufficient for the transaction.
     */
    public void credit(Account account, DepositTransaction transaction) throws InsufficientBalanceException {
        // NOTE: Test sırasında mocklandığı için bu şekilde implementasyona izin vermiyor.
//        Account account = findAccount(accountNumber);
        account.post(transaction);
        transaction.setAccount(account);
        transaction.setApprovalCode(UUID.randomUUID().toString());
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    /**
     * Debits the specified account with the provided withdrawal transaction.
     *
     * @param account      The account to debit.
     * @param transaction  The withdrawal transaction to perform.
     * @throws InsufficientBalanceException If the account balance is insufficient for the transaction.
     */
    public void debit(Account account, WithdrawalTransaction transaction) throws InsufficientBalanceException {
        // NOTE: Test sırasında mocklandığı için bu şekilde implementasyona izin vermiyor.
        // Account account = findAccount(accountNumber);
        account.post(transaction);
        transaction.setAccount(account);
        transaction.setApprovalCode(UUID.randomUUID().toString());
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    /**
     * Performs a bill payment transaction for the specified account.
     *
     * @param account      The account to pay the bill from.
     * @param transaction  The bill payment transaction to perform.
     * @throws InsufficientBalanceException If the account balance is insufficient for the transaction.
     */
    public void bill(Account account, BillPaymentTransaction transaction) throws InsufficientBalanceException {
        account.post(transaction);
        transaction.setAccount(account);
        transaction.setApprovalCode(UUID.randomUUID().toString());
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    /**
     * Creates a new account and saves it to the database.
     *
     * @param account The account to be created.
     * @return The created account.
     */
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Retrieves account details along with transaction history for the specified account.
     *
     * @param account The account to retrieve details for.
     * @return An {@code AccountResponse} object containing account information and transaction history.
     */
    public AccountResponse getAccount(Account account) {
        List<Transaction> transactions = transactionRepository.findAllByAccountId(account.getId());
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .owner(account.getOwner())
                .createDate(account.getCreatedDate())
                .transactions(transactions.stream().map(x ->
                        AccountTransactionItem.builder()
                                .amount(x.getAmount())
                                .approvalCode(x.getApprovalCode())
                                .date(x.getDate())
                                .type(x.getTransactionType())
                                .build()
                ).collect(Collectors.toList())).build();

    }

}
