package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.WalletRequestDTO;
import org.example.dto.WalletResponseDTO;
import org.example.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.example.constants.ApiConstants.API_V1_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_PATH)

public class WalletController {

    private final WalletService walletService;

    @PostMapping("/wallet")
    public ResponseEntity<WalletResponseDTO> updateBalance(@RequestBody WalletRequestDTO requestDTO) {
        WalletResponseDTO responseDTO = walletService.doTransaction
                (requestDTO.getValletId(), requestDTO.getAmount(), requestDTO.getOperationType());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<WalletResponseDTO> archiveTask(@PathVariable("WALLET_UUID") UUID walletId) {
        WalletResponseDTO responseDTO = walletService.getBalance(walletId);
        return ResponseEntity.ok(responseDTO);
    }
}
