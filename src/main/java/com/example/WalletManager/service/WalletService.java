package com.example.WalletManager.service;

import com.example.WalletManager.dto.BalanceOperationDto;
import com.example.WalletManager.entity.Wallet;
import com.example.WalletManager.exceprion.InsufficientFundsException;
import com.example.WalletManager.exceprion.WalletNotFoundException;
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
    public void performOperation(BalanceOperationDto walletDto) {
        Wallet wallet = walletRepository.findByWalletId(walletDto.getWalletId());

        if (wallet == null) {
            throw new WalletNotFoundException("Wallet not found");
        }

        switch (walletDto.getType()) {
            case DEPOSIT -> wallet.setBalance(wallet.getBalance().add(walletDto.getAmount()));
            case WITHDRAW -> {

                if (wallet.getBalance().compareTo(walletDto.getAmount()) < 0) {
                    throw new InsufficientFundsException("Insufficient founds");
                }
                wallet.setBalance(wallet.getBalance().subtract(walletDto.getAmount()));
            }
        }

        walletRepository.save(wallet);
    }

    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.readByWalletId(walletId);

        if (wallet == null) {
            throw new WalletNotFoundException("Wallet not found");
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
