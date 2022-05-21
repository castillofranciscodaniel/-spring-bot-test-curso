package org.castillofranciscodaniel.springboot_test.controller;

import lombok.AllArgsConstructor;
import org.castillofranciscodaniel.springboot_test.models.Account;
import org.castillofranciscodaniel.springboot_test.models.TransactionDto;
import org.castillofranciscodaniel.springboot_test.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;

    @GetMapping(name = "findById", path = "{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(this.accountService.findById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping(path = "transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionDto bodyRequest) {
        try {
            this.accountService.transfer(bodyRequest.getOriginAccountNumber(), bodyRequest.getDestinationAccountNumber(), bodyRequest.getAmount(), bodyRequest.getBankId());

            Map<String, Object> response = new HashMap<>();
            response.put("date", LocalDate.now().toString());
            response.put("status", "ok");
            response.put("message", "Transfer ok!");
            response.put("transaction", bodyRequest);

            return ResponseEntity.ok().body(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> list() {
        return ResponseEntity.ok(this.accountService.findAll());
    }

    @PostMapping
    public ResponseEntity<Account> save(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.accountService.save(account));
    }


}
