package com.example.WalletManager.service;

import com.example.WalletManager.dto.WalletDto;
import com.example.WalletManager.entity.Wallet;
import com.example.WalletManager.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void performOperation(WalletDto walletDto) {
        Wallet wallet = walletRepository.findByWalletId(walletDto.getWalletId());
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found");
        }

        switch (walletDto.getOperationType()) {
            case DEPOSIT:
                wallet.setBalance(wallet.getBalance().add(walletDto.getAmount()));
                break;
            case WITHDRAW:
                if (wallet.getBalance().compareTo(walletDto.getAmount()) < 0) {
                    throw new IllegalArgumentException("Insufficient founds");
                }
                wallet.setBalance(wallet.getBalance().subtract(walletDto.getAmount()));
                break;
        }

        walletRepository.save(wallet);
    }

    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findByWalletId(walletId);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found");
        }
        return wallet.getBalance();
    }

    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);
        return wallet;
    }
}
