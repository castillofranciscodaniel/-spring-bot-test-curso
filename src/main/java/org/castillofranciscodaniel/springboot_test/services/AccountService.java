package org.castillofranciscodaniel.springboot_test.services;

import org.castillofranciscodaniel.springboot_test.models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public interface AccountService {

    Account findById(Long id) throws NoSuchElementException;

    List<Account> findAll();

    Account save(Account account);

    int checkTransferTotal(Long bankId);

    BigDecimal checkBalance(Long accountId);

    void transfer(Long originAccountNumber, Long destinationAccountNumber, BigDecimal amount, Long bankId);

}
