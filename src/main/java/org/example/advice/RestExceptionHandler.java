package org.example.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.WalletResponseDTO;
import org.example.exception.MainException;
import org.example.exception.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MainException.class)
    public ResponseEntity<WalletResponseDTO> handleMainException(MainException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        WalletResponseDTO walletResponseDTO = new WalletResponseDTO(exception.getMessage());

        return new ResponseEntity<>(walletResponseDTO, status);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<WalletResponseDTO> handleWalletNotFoundException(WalletNotFoundException exception) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        WalletResponseDTO walletResponseDTO = new WalletResponseDTO(exception.getMessage());

        return new ResponseEntity<>(walletResponseDTO, status);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<WalletResponseDTO> handleException(Exception exception) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        WalletResponseDTO walletResponseDTO = new WalletResponseDTO(exception.getMessage());

        return new ResponseEntity<>(walletResponseDTO, status);
    }
}