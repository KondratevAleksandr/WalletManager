package com.example.WalletManager.controller;

import com.example.WalletManager.dto.ApiResponse;
import com.example.WalletManager.dto.WalletDto;
import com.example.WalletManager.entity.Wallet;
import com.example.WalletManager.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<ApiResponse> performOperation(@RequestBody WalletDto walletDto) {
        try {
            walletService.performOperation(walletDto);
            return ResponseEntity.ok(new ApiResponse("Operation completed successfully", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("An unexpected error occurred", false));
        }
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<ApiResponse> getBalance(@PathVariable UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        if (balance == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Wallet not found", false));
        }
        return ResponseEntity.ok(new ApiResponse("Balance for wallet " + walletId + " is " + balance, true));
    }

    @GetMapping("/create")
    public ResponseEntity<ApiResponse> createWallet() {
        Wallet wallet = walletService.createWallet();
        return ResponseEntity.ok(new ApiResponse("Wallet created with ID: " + wallet.getWalletId(), true));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid request format", false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("An unexpected error", false));
    }
}
