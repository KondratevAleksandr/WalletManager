package com.example.WalletManager.repository;

import com.example.WalletManager.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByWalletId(UUID walletId);
}
