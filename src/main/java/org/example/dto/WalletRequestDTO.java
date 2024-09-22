package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletRequestDTO {

    private UUID valletId; // явная опечатка в требованиях, но вдруг так надо

    private String operationType;

    private BigDecimal amount;
}
