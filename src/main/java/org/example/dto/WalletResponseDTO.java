package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {

    private boolean hasError;

    private UUID walletId;

    private BigDecimal balance;

    private String message;

    public WalletResponseDTO(UUID walletId, BigDecimal balance) {
        this.hasError = false;
        this.walletId = walletId;
        this.balance = balance;
    }

    public WalletResponseDTO(String message) {
        this.hasError = true;
        this.message = message;
    }
}
