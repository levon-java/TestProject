package org.example.service;
import org.example.entity.Wallet;
import org.example.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Test
    void testConcurrentTransactionsWithDifferentWalletIds() throws InterruptedException {
        UUID walletId1 = UUID.randomUUID();
        Wallet wallet1 = new Wallet();
        wallet1.setId(walletId1);
        wallet1.setBalance(BigDecimal.valueOf(1000));
        when(walletRepository.findById(walletId1)).thenReturn(Optional.of(wallet1));

        UUID walletId2 = UUID.randomUUID();
        Wallet wallet2 = new Wallet();
        wallet2.setId(walletId2);
        wallet2.setBalance(BigDecimal.valueOf(1000));
        when(walletRepository.findById(walletId2)).thenReturn(Optional.of(wallet2));

        BigDecimal depositAmount = BigDecimal.valueOf(500);
        int numberOfThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads * 2);

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    walletService.doTransaction(walletId1, depositAmount, "DEPOSIT");
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    walletService.doTransaction(walletId2, depositAmount, "DEPOSIT");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(
                BigDecimal.valueOf(1000).add(depositAmount.multiply(BigDecimal.valueOf(numberOfThreads))),
                wallet1.getBalance()
        );

        assertEquals(
                BigDecimal.valueOf(1000).add(depositAmount.multiply(BigDecimal.valueOf(numberOfThreads))),
                wallet2.getBalance()
        );

        executor.shutdown();
    }
}
