package org.castillofranciscodaniel.springboot_test.services;

import lombok.AllArgsConstructor;
import org.castillofranciscodaniel.springboot_test.models.Account;
import org.castillofranciscodaniel.springboot_test.models.Bank;
import org.castillofranciscodaniel.springboot_test.repositories.AccountRepository;
import org.castillofranciscodaniel.springboot_test.repositories.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private BankRepository bankRepository;

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) throws NoSuchElementException {
        return this.accountRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        return this.accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account save(Account account) {
        return this.accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public int checkTransferTotal(Long bankId) throws NoSuchElementException {
        return this.bankRepository.findById(bankId).orElseThrow().getTransferTotal();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long accountId) throws NoSuchElementException {
        return this.accountRepository.findById(accountId).orElseThrow().getBalance();
    }

    @Override
    @Transactional
    public void transfer(Long originAccountNumber, Long destinationAccountNumber, BigDecimal amount, Long bankId) throws NoSuchElementException {
        Account originAccount = this.accountRepository.findById(originAccountNumber).orElseThrow();
        originAccount.debit(amount);
        this.accountRepository.save(originAccount);

        Account destinationAccount = this.accountRepository.findById(destinationAccountNumber).orElseThrow();
        destinationAccount.credit(amount);
        this.accountRepository.save(destinationAccount);

        Bank bank = this.bankRepository.findById(bankId).orElseThrow();
        int totalTransfers = bank.getTransferTotal();
        bank.setTransferTotal(++totalTransfers);
        this.bankRepository.save(bank);

    }
}
