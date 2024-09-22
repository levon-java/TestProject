package org.example.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionConstants {

    public static final String ERROR_INSUFFICIENT_FUNDS = "Insufficient funds";

    public static final String ERROR_WALLET_NOT_FOUND = "Wallet not found";

    public static final String ERROR_UNKNOWN_TYPE_OF_OPERATION = "Unknown type of operation: '%s'";
}
