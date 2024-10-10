package com.example.WalletManager.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class WalletDto {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;

}
