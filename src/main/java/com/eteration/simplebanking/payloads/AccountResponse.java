package com.eteration.simplebanking.payloads;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AccountResponse {
    private String accountNumber;
    private String owner;
    private double balance;
    private LocalDateTime createDate;
    private List<AccountTransactionItem> transactions;
}
