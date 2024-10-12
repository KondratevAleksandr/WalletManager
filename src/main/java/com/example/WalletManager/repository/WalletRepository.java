package com.example.WalletManager.repository;

import com.example.WalletManager.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet readByWalletId(UUID walletId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Wallet findByWalletId(UUID walletId);
}
