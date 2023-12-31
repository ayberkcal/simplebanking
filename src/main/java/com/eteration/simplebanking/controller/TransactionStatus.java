package com.eteration.simplebanking.controller;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionStatus {
    private String status;
    private String approvalCode;

}
