package org.castillofranciscodaniel.springboot_test.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.castillofranciscodaniel.springboot_test.Data.*;

import org.castillofranciscodaniel.springboot_test.exceptions.InsufficientMoneyException;
import org.castillofranciscodaniel.springboot_test.models.Account;
import org.castillofranciscodaniel.springboot_test.models.Bank;
import org.castillofranciscodaniel.springboot_test.repositories.AccountRepository;
import org.castillofranciscodaniel.springboot_test.repositories.BankRepository;
import org.castillofranciscodaniel.springboot_test.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AccountServiceTest {

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    BankRepository bankRepository;

    @Autowired
    AccountService accountService;

    @Test
    void contextLoads_test() {
        // given
        when(this.accountRepository.findById(1L)).thenReturn(Optional.of(createAccount001()));
        when(this.accountRepository.findById(2L)).thenReturn(Optional.of(createAccount002()));
        when(this.bankRepository.findById(1L)).thenReturn(Optional.of(createBank()));

        // when
        BigDecimal originBalance = accountService.checkBalance(1L);
        BigDecimal destinationBalance = accountService.checkBalance(2L);

        assertEquals("1000", originBalance.toString());
        assertEquals("2000", destinationBalance.toString());

        accountService.transfer(1L, 2L, new BigDecimal("100"), 1L);

        originBalance = this.accountService.checkBalance(1l);
        destinationBalance = this.accountService.checkBalance(2l);

        assertEquals("900", originBalance.toString());
        assertEquals("2100", destinationBalance.toString());

        int transferTotal = this.accountService.checkTransferTotal(1L);

        // assert
        assertEquals(1, transferTotal);

        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(3)).findById(2L);
        verify(accountRepository, times(2)).save(any(Account.class));

        verify(bankRepository, times(2)).findById(1L);
        verify(bankRepository).save(any(Bank.class));

        verify(accountRepository, times(6)).findById(anyLong());

    }

    @Test
    void contextLoads2_test() {
        // given
        when(this.accountRepository.findById(1L)).thenReturn(Optional.of(createAccount001()));
        when(this.accountRepository.findById(2L)).thenReturn(Optional.of(createAccount002()));
        when(this.bankRepository.findById(1L)).thenReturn(Optional.of(createBank()));

        // when
        BigDecimal originBalance = accountService.checkBalance(1L);
        BigDecimal destinationBalance = accountService.checkBalance(2L);

        assertEquals("1000", originBalance.toString());
        assertEquals("2000", destinationBalance.toString());

        assertThrows(InsufficientMoneyException.class, () -> accountService.transfer(1L, 2L, new BigDecimal("1200"), 1L));


        originBalance = this.accountService.checkBalance(1l);
        destinationBalance = this.accountService.checkBalance(2l);

        assertEquals("1000", originBalance.toString());
        assertEquals("2000", destinationBalance.toString());

        int transferTotal = this.accountService.checkTransferTotal(1L);
        assertEquals(0, transferTotal);

        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(2)).findById(2L);
        verify(accountRepository, never()).save(any(Account.class));

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, never()).save(any(Bank.class));


        verify(accountRepository, times(5)).findById(anyLong());

    }

    @Test
    void contextLoads3_test() {
        // given
        when(this.accountRepository.findById(1L)).thenReturn(Optional.of(createAccount001()));

        Account account001 = this.accountService.findById(1L);
        Account account002 = this.accountService.findById(1L);

        assertSame(account001, account002);

        verify(this.accountRepository, times(2)).findById(1L);

    }

    @Test
    void findAll_test() {
        // given
        List<Account> accounts = Arrays.asList(createAccount001(), createAccount002());

        when(this.accountRepository.findAll()).thenReturn(accounts);

        // when
        List<Account> accounts1 = this.accountService.findAll();

        assertFalse(accounts1.isEmpty());
        assertEquals(2, accounts1.size());
        assertTrue(accounts1.contains(createAccount001()));

        verify(this.accountRepository).findAll();
    }

    @Test
    void save_test() {
        // given

        when(this.accountRepository.save(createAccount001())).then(invocationOnMock -> {
            Account account = invocationOnMock.getArgument(0);
            account.setId(3L);
            return account;
        });

        // when
        Account accounts1 = this.accountService.save(createAccount001());

        assertEquals(3L, accounts1.getId());
        assertEquals("Andrey", accounts1.getPerson());
        assertEquals("1000", accounts1.getBalance().toPlainString());

        verify(this.accountRepository).save(any());
    }
}
