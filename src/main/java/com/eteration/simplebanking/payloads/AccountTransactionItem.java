package com.eteration.simplebanking.payloads;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AccountTransactionItem {
    private LocalDateTime date;
    private double amount;
    private String type;
    private String approvalCode;
}