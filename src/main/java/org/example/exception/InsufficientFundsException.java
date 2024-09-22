package org.example.exception;

import static org.example.constants.ExceptionConstants.ERROR_INSUFFICIENT_FUNDS;

public class InsufficientFundsException extends MainException {
    public InsufficientFundsException() {
        super(ERROR_INSUFFICIENT_FUNDS);
    }
}
