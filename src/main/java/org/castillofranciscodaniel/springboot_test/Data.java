package org.castillofranciscodaniel.springboot_test;

import org.castillofranciscodaniel.springboot_test.models.Account;
import org.castillofranciscodaniel.springboot_test.models.Bank;

import java.math.BigDecimal;

public class Data {

    public static Account createAccount001() {
        return new Account(1L, "Andrey", new BigDecimal("1000"));
    }

    public static Account createAccount002() {
        return new Account(2L, "Jon", new BigDecimal("2000"));
    }

    public static Bank createBank() {
        return new Bank(1L, "financial bank", 0);
    }
}
