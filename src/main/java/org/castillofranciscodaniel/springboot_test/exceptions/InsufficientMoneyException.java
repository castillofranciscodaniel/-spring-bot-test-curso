package org.castillofranciscodaniel.springboot_test.exceptions;

public class InsufficientMoneyException extends RuntimeException {

    public InsufficientMoneyException(String message) {
        super(message);
    }

}
