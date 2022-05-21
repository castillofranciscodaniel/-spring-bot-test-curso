package org.castillofranciscodaniel.springboot_test.repositories;

import org.castillofranciscodaniel.springboot_test.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByPersonIgnoreCase(@NonNull String person);
}
