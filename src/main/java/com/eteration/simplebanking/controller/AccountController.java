package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.payloads.AccountResponse;
import com.eteration.simplebanking.services.AccountNotFoundException;
import com.eteration.simplebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * The {@code AccountController} class defines RESTful endpoints for account-related operations.
 * It serves as the entry point for handling HTTP requests related to account management.
 *
 * <p>The class includes endpoints for crediting, debiting, bill payment, retrieving account details,
 * and creating new accounts. Each endpoint corresponds to a specific operation in the {@link AccountService}.
 *
 * <p>Dependencies:
 * - {@link AccountService}: Provides business logic for account and transaction operations.
 *
 * <p>Usage example:
 * <pre>
 * {@code
 *   AccountController accountController = new AccountController(accountService);
 *   ResponseEntity<TransactionStatus> response = accountController.credit("123456789", new DepositTransaction(100.0));
 * }
 * </pre>
 */
@RestController
@RequestMapping("/account/v1/")
@Transactional
public class AccountController {

    private final AccountService accountService;

    /**
     * Constructs an {@code AccountController} with the specified account service.
     *
     * @param accountService The service providing account-related operations.
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Handles HTTP POST requests for crediting an account.
     *
     * @param accountNumber The account number to credit.
     * @param request       The deposit transaction details.
     * @return A {@code ResponseEntity} with the transaction status.
     * @throws InsufficientBalanceException If the account balance is insufficient for the transaction.
     * @throws AccountNotFoundException     If the specified account is not found.
     */
    @PostMapping(value = "/credit/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionStatus> credit(@PathVariable("accountNumber") String accountNumber, @RequestBody DepositTransaction request) throws InsufficientBalanceException, AccountNotFoundException {
        Account account = accountService.findAccount(accountNumber);
        // Rest API için:
        // buradaki yorum satırını kaldırıp alt satırı yorum satırı haline getirin.
        accountService.credit(account, request);
        // Test için:
        // buradaki yorum satırını kaldırıp üst satırı yorum satırı haline getirin.
        // account.post(request);
        return ResponseEntity.ok(TransactionStatus.builder().status("OK").approvalCode(UUID.randomUUID().toString()).build());
    }

    /**
     * Handles HTTP POST requests for debiting an account.
     *
     * @param accountNumber The account number to debit.
     * @param request       The withdrawal transaction details.
     * @return A {@code ResponseEntity} with the transaction status.
     * @throws InsufficientBalanceException If the account balance is insufficient for the transaction.
     * @throws AccountNotFoundException     If the specified account is not found.
     */
    @PostMapping(value = "/debit/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionStatus> debit(@PathVariable("accountNumber") String accountNumber, @RequestBody WithdrawalTransaction request) throws InsufficientBalanceException, AccountNotFoundException {
        Account account = accountService.findAccount(accountNumber);
        // Rest API için:
        // buradaki yorum satırını kaldırıp alt satırı yorum satırı haline getirin.
        accountService.debit(account, request);
        // Test için:
        // buradaki yorum satırını kaldırıp üst satırı yorum satırı haline getirin.
        // account.post(request);
        return ResponseEntity.ok(TransactionStatus.builder().status("OK").approvalCode(UUID.randomUUID().toString()).build());
    }

    /**
     * Handles HTTP POST requests for bill payment from an account.
     *
     * @param accountNumber The account number to make the bill payment from.
     * @param request       The bill payment transaction details.
     * @return A {@code ResponseEntity} with the transaction status.
     * @throws InsufficientBalanceException If the account balance is insufficient for the transaction.
     * @throws AccountNotFoundException     If the specified account is not found.
     */
    @PostMapping(value = "/bill/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionStatus> billPayment(@PathVariable("accountNumber") String accountNumber, @RequestBody BillPaymentTransaction request) throws InsufficientBalanceException, AccountNotFoundException {
        Account account = accountService.findAccount(accountNumber);
        // Rest API için:
        // buradaki yorum satırını kaldırıp alt satırı yorum satırı haline getirin.
        accountService.bill(account, request);
        // Test için:
        // buradaki yorum satırını kaldırıp üst satırı yorum satırı haline getirin.
        // account.post(request);
        return ResponseEntity.ok(TransactionStatus.builder().status("OK").approvalCode(UUID.randomUUID().toString()).build());
    }

    /**
     * Handles HTTP GET requests for retrieving account details.
     *
     * @param accountNumber The account number to retrieve details for.
     * @return A {@code ResponseEntity} with the account details.
     * @throws AccountNotFoundException If the specified account is not found.
     */
    @GetMapping(value = "/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("accountNumber") String accountNumber) throws AccountNotFoundException {
        Account account = accountService.findAccount(accountNumber);
        AccountResponse result = accountService.getAccount(account);
        return ResponseEntity.ok(result);
    }

    /**
     * Handles HTTP POST requests for creating a new account.
     *
     * @param account The account details for creation.
     * @return A {@code ResponseEntity} with the created account details.
     */
    @PostMapping(value = "/account/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return ResponseEntity.ok(createdAccount);
    }
}