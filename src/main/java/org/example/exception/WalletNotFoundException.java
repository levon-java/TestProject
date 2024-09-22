package org.example.exception;


import static org.example.constants.ExceptionConstants.ERROR_WALLET_NOT_FOUND;

public class WalletNotFoundException extends MainException {
    public WalletNotFoundException() {
        super(ERROR_WALLET_NOT_FOUND);
    }
}
