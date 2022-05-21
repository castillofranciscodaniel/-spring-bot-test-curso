package org.castillofranciscodaniel.springboot_test.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.castillofranciscodaniel.springboot_test.models.Account;
import org.castillofranciscodaniel.springboot_test.repositories.AccountRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
public class JpaIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findById_test() {
        Optional<Account> accounts = this.accountRepository.findById(1L);
        assertTrue(accounts.isPresent());
        assertEquals("Andrey", accounts.orElseThrow().getPerson());
    }

    @Test
    void findByPersonExist_test() {
        List<Account> accounts = this.accountRepository.findByPersonIgnoreCase("Andrey");
        assertTrue(!accounts.isEmpty());
        assertEquals("1000.00", accounts.get(0).getBalance().toPlainString());
    }

    @Test
    void findByPersonNotExist_test() {
        List<Account> accounts = this.accountRepository.findByPersonIgnoreCase("Roy");
        assertTrue(accounts.isEmpty());
    }

    @Test
    void findAll_test() {
        List<Account> accounts = this.accountRepository.findAll();
        assertTrue(!accounts.isEmpty());
        assertEquals(2, accounts.size());
    }

    @Test
    void save_test() {
        Account account = Account.builder().person("Pep").balance(new BigDecimal("3000")).build();
        Account saved = this.accountRepository.save(account);
        assertEquals("Pep", saved.getPerson());
        assertEquals("3000", saved.getBalance().toPlainString());
        assertNotNull(saved.getId());
    }

    @Test
    void updated_test() {
        Account account = Account.builder().person("Pep").balance(new BigDecimal("3000")).build();
        Account saved = this.accountRepository.save(account);

        saved.setPerson("Mauro");

        Account updated = this.accountRepository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Mauro", updated.getPerson());
    }

    @Test
    void delete_test() {
        Long id = 2L;
        assertTrue(this.accountRepository.findById(id).isPresent());
        this.accountRepository.deleteById(id);
        assertThrows(NoSuchElementException.class, () -> this.accountRepository.findById(id).orElseThrow());
    }
}
