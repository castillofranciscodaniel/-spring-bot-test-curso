package org.castillofranciscodaniel.springboot_test.repositories;

import org.castillofranciscodaniel.springboot_test.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BankRepository extends JpaRepository<Bank, Long> {


}
