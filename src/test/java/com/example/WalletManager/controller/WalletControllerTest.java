package com.example.WalletManager.controller;

import com.example.WalletManager.dto.BalanceOperationDto;
import com.example.WalletManager.dto.OperationType;
import com.example.WalletManager.entity.Wallet;
import com.example.WalletManager.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    public void testPerformDepositOperation() throws Exception {
        UUID walletId = UUID.randomUUID();
        BalanceOperationDto walletDto = new BalanceOperationDto();
        walletDto.setWalletId(walletId);
        walletDto.setAmount(BigDecimal.valueOf(100));
        walletDto.setType(OperationType.DEPOSIT);

        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.ZERO);

        doAnswer(invocation -> {
            BalanceOperationDto operationDto = invocation.getArgument(0);
            if (OperationType.DEPOSIT.equals(operationDto.getType())) {
                wallet.setBalance(wallet.getBalance().add(operationDto.getAmount()));
            }
            return null;
        }).when(walletService).performOperation(any(BalanceOperationDto.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\",\"amount\":100,\"type\":\"DEPOSIT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operation completed successfully"))
                .andExpect(jsonPath("$.success").value(true));

        verify(walletService, times(1)).performOperation(any(BalanceOperationDto.class));
    }

    @Test
    public void testPerformWithdrawOperation() throws Exception {
        UUID walletId = UUID.randomUUID();
        BalanceOperationDto walletDto = new BalanceOperationDto();
        walletDto.setWalletId(walletId);
        walletDto.setAmount(BigDecimal.valueOf(50));
        walletDto.setType(OperationType.WITHDRAW);

        Wallet wallet = new Wallet();
        wallet.setWalletId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        doAnswer(invocation -> {
            BalanceOperationDto operationDto = invocation.getArgument(0);
            if (OperationType.WITHDRAW.equals(operationDto.getType())) {
                wallet.setBalance(wallet.getBalance().subtract(operationDto.getAmount()));
            }
            return null;
        }).when(walletService).performOperation(any(BalanceOperationDto.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"walletId\":\"" + walletId + "\",\"amount\":50,\"type\":\"WITHDRAW\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Operation completed successfully"))
                .andExpect(jsonPath("$.success").value(true));

        verify(walletService, times(1)).performOperation(any(BalanceOperationDto.class));
    }

    @Test
    public void testGetBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(100);

        when(walletService.getBalance(walletId)).thenReturn(balance);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/wallet/wallets/{walletId}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Balance for wallet " + walletId + " is " + balance))
                .andExpect((jsonPath("$.success").value(true)));

        verify(walletService, times(1)).getBalance(walletId);
    }


    @Test
    public void testCreateWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID());

        when(walletService.createWallet()).thenReturn(wallet);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/wallet/create")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Wallet created with ID: " + wallet.getWalletId()))
                .andExpect((jsonPath("$.success").value(true)));
        verify(walletService, times(1)).createWallet();
    }
}
