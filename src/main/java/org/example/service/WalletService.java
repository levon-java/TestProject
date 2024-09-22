package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.WalletResponseDTO;
import org.example.entity.Wallet;
import org.example.exception.InsufficientFundsException;
import org.example.exception.MainException;
import org.example.exception.WalletNotFoundException;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import static org.example.constants.ExceptionConstants.ERROR_UNKNOWN_TYPE_OF_OPERATION;
import static org.example.constants.OperationType.DEPOSIT;
import static org.example.constants.OperationType.WITHDRAW;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final Map<UUID, Object> lockMap = Collections.synchronizedMap(new WeakHashMap<>());

    @Transactional
    public WalletResponseDTO doTransaction(UUID walletId, BigDecimal amount, String operationType) {
        Object lock = lockMap.computeIfAbsent(walletId, id -> new Object());

        BigDecimal newBalance;

        synchronized (lock) {
            Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);

            switch (operationType) {
                case DEPOSIT -> wallet.setBalance(wallet.getBalance().add(amount));
                case WITHDRAW -> {
                    if (wallet.getBalance().compareTo(amount) < 0) {
                        throw new InsufficientFundsException();
                    } else {
                        wallet.setBalance(wallet.getBalance().subtract(amount));
                    }
                }
                default -> throw new MainException(String.format(ERROR_UNKNOWN_TYPE_OF_OPERATION, operationType));
            }

            newBalance = wallet.getBalance();

            walletRepository.save(wallet);
        }
        return new WalletResponseDTO(walletId, newBalance);
    }

    public WalletResponseDTO getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        return new WalletResponseDTO(wallet.getId(), wallet.getBalance());
    }
}

