package org.example.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.example.dto.WalletRequestDTO;
import org.example.dto.WalletResponseDTO;
import org.example.initializer.PostgresInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.example.constants.ApiConstants.API_V1_PATH;
import static org.example.constants.ExceptionConstants.ERROR_INSUFFICIENT_FUNDS;
import static org.example.constants.ExceptionConstants.ERROR_UNKNOWN_TYPE_OF_OPERATION;
import static org.example.constants.ExceptionConstants.ERROR_WALLET_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@DBRider
@DBUnit(caseSensitiveTableNames = true, schema = "public")
@ContextConfiguration(initializers = PostgresInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletControllerTest {

    @Autowired
    protected WebTestClient webTestClient;

    UUID correctID = UUID.fromString("e6e9d88a-9b63-468a-aec3-b7a11de27af8");

    @Test
    @DataSet(value = "CorrectWallets.yml", cleanAfter = true)
    void shouldReturn404_WhenWalletIdDoesNotExist() {
        WalletResponseDTO responseBody = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(API_V1_PATH + "/wallets/{WALLET_UUID}")
                        .build(UUID.randomUUID()))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WalletResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertTrue(responseBody.isHasError());
        assertNull(responseBody.getWalletId());
        assertNull(responseBody.getBalance());
        assertEquals(ERROR_WALLET_NOT_FOUND, responseBody.getMessage());
    }

    @Test
    @DataSet(value = "CorrectWallets.yml", cleanAfter = true)
    void shouldReturn200_WhenWalletIdExists() {
        WalletResponseDTO responseBody = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(API_V1_PATH + "/wallets/{WALLET_UUID}")
                        .build(correctID))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertFalse(responseBody.isHasError());
        assertEquals(correctID, responseBody.getWalletId());
        assertEquals(BigDecimal.valueOf(1000), responseBody.getBalance());
        assertNull(responseBody.getMessage());
    }

    @Test
    @DataSet(value = "CorrectWallets.yml", cleanAfter = true)
    void shouldReturn400_WhenOperationTypeDoesNotExist() {
        WalletRequestDTO walletRequestDTO =
                new WalletRequestDTO(correctID, "Fake Type", BigDecimal.valueOf(200));

        WalletResponseDTO responseBody = webTestClient.post()
                .uri(API_V1_PATH + "/wallet")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(walletRequestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WalletResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertTrue(responseBody.isHasError());
        assertNull(responseBody.getWalletId());
        assertNull(responseBody.getBalance());
        assertEquals(String.format(ERROR_UNKNOWN_TYPE_OF_OPERATION, walletRequestDTO.getOperationType()),
                responseBody.getMessage());
    }


    @Test
    @DataSet(value = "CorrectWallets.yml", cleanAfter = true)
    void shouldReturn200_WhenOperationTypeDeposit() {
        WalletRequestDTO walletRequestDTO =
                new WalletRequestDTO(correctID, "DEPOSIT", BigDecimal.valueOf(200));

        WalletResponseDTO responseBody = webTestClient.post()
                .uri(API_V1_PATH + "/wallet")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(walletRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertFalse(responseBody.isHasError());
        assertEquals(walletRequestDTO.getValletId(), responseBody.getWalletId());
        assertEquals(BigDecimal.valueOf(1200), responseBody.getBalance());
        assertNull(responseBody.getMessage());
    }

    @Test
    @DataSet(value = "CorrectWallets.yml", cleanAfter = true)
    void shouldReturn200_WhenOperationTypeWithdraw() {
        WalletRequestDTO walletRequestDTO =
                new WalletRequestDTO(correctID, "WITHDRAW", BigDecimal.valueOf(200));

        WalletResponseDTO responseBody = webTestClient.post()
                .uri(API_V1_PATH + "/wallet")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(walletRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertFalse(responseBody.isHasError());
        assertEquals(walletRequestDTO.getValletId(), responseBody.getWalletId());
        assertEquals(BigDecimal.valueOf(800), responseBody.getBalance());
        assertNull(responseBody.getMessage());
    }

    @Test
    @DataSet(value = "CorrectWallets.yml", cleanAfter = true)
    void shouldReturn400_WhenInsufficientFunds() {
        WalletRequestDTO walletRequestDTO =
                new WalletRequestDTO(correctID, "WITHDRAW", BigDecimal.valueOf(1200));

        WalletResponseDTO responseBody = webTestClient.post()
                .uri(API_V1_PATH + "/wallet")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(walletRequestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WalletResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertTrue(responseBody.isHasError());
        assertNull(responseBody.getWalletId());
        assertNull(responseBody.getBalance());
        assertEquals(ERROR_INSUFFICIENT_FUNDS, responseBody.getMessage());
    }

    @Test
    @DataSet(value = "CorrectWallets.yml", cleanAfter = true)
    void shouldReturn500_WhenRuntimeException() {
        WalletRequestDTO walletRequestDTO =
                new WalletRequestDTO(null, "WITHDRAW", BigDecimal.valueOf(1200));

        WalletResponseDTO responseBody = webTestClient.post()
                .uri(API_V1_PATH + "/wallet")
                .accept(APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(walletRequestDTO)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(WalletResponseDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertTrue(responseBody.isHasError());
        assertNull(responseBody.getWalletId());
        assertNull(responseBody.getBalance());
        assertNotNull(responseBody.getMessage());
    }
}
