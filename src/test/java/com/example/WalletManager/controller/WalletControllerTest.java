package com.example.WalletManager.controller;

import com.example.WalletManager.dto.BalanceOperationDto;
import com.example.WalletManager.dto.OperationType;
import com.example.WalletManager.entity.Wallet;
import com.example.WalletManager.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void testPerformOperation() throws Exception {
        BalanceOperationDto walletDto = new BalanceOperationDto();
        walletDto.setWalletId(UUID.randomUUID());
        walletDto.setType(OperationType.DEPOSIT);
        walletDto.setAmount(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletDto.getWalletId() + "\", \"amount\":100, \"type\":\"DEPOSIT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operation completed successfully"));

        verify(walletService, times(1)).performOperation(any(BalanceOperationDto.class));
    }

    @Test
    public void testGetBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(150.00);

        when(walletService.getBalance(walletId)).thenReturn(balance);

        mockMvc.perform(get("/api/v1/wallet/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Balance for wallet " + walletId + " is " + balance));

        verify(walletService, times(1)).getBalance(walletId);
    }

    @Test
    public void testCreateWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID());

        when(walletService.createWallet()).thenReturn(wallet);

        mockMvc.perform(get("/api/v1/wallet/create"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Wallet created with ID: " + wallet.getWalletId()));

        verify(walletService, times(1)).createWallet();
    }


}
