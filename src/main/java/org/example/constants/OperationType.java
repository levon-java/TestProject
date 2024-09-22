package org.example.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationType {

    /**
     * Я бы использовал enum, но многие против такого подхода, так что константы
     */
    public static final String DEPOSIT = "DEPOSIT";

    public static final String WITHDRAW = "WITHDRAW";
}
