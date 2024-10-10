package com.example.WalletManager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id", unique = true, nullable = false)
    private UUID walletId;

    @Column(name = "balance")
    private BigDecimal balance;

}
