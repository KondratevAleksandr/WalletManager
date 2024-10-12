package com.example.WalletManager.controller;

import com.example.WalletManager.dto.ApiResponse;
import com.example.WalletManager.dto.BalanceOperationDto;
import com.example.WalletManager.entity.Wallet;
import com.example.WalletManager.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ApiResponse performOperation(@Validated @RequestBody BalanceOperationDto walletDto) {
        walletService.performOperation(walletDto);
        return new ApiResponse("Operation completed successfully", true);
    }

    @GetMapping("/wallets/{walletId}")
    public ApiResponse getBalance(@PathVariable UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        return new ApiResponse("Balance for wallet " + walletId + " is " + balance, true);
    }

    @GetMapping("/create")
    public ApiResponse createWallet() {
        Wallet wallet = walletService.createWallet();
        return new ApiResponse("Wallet created with ID: " + wallet.getWalletId(), true);
    }
}
