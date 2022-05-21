package org.castillofranciscodaniel.springboot_test.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.castillofranciscodaniel.springboot_test.Data;
import org.castillofranciscodaniel.springboot_test.models.Account;
import org.castillofranciscodaniel.springboot_test.models.TransactionDto;
import org.castillofranciscodaniel.springboot_test.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_test() throws Exception {
        // given
        when(this.accountService.findById(1L)).thenReturn(Data.createAccount001());

        // when
        this.mockMvc.perform(get("/api/accounts/1").contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Andrey"))
                .andExpect(jsonPath("$.balance").value("1000"));

        verify(this.accountService).findById(1L);
    }

    @Test
    void transfer_test() throws Exception {

        TransactionDto transactionDto = TransactionDto.builder()
                .originAccountNumber(1L)
                .destinationAccountNumber(2L)
                .amount(new BigDecimal("100"))
                .bankId(1L)
                .build();


        Map<String, Object> responseExpected = new HashMap<>();
        responseExpected.put("date", LocalDate.now().toString());
        responseExpected.put("status", "ok");
        responseExpected.put("message", "Transfer ok!");
        responseExpected.put("transaction", transactionDto);

        this.mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(transactionDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.transaction.originAccountNumber").value(1L))
                .andExpect(content().json(this.objectMapper.writeValueAsString(responseExpected)))
        ;
    }

    @Test
    void list_test() throws Exception {
        List<Account> accounts = Arrays.asList(Data.createAccount001(), Data.createAccount002());

        when(this.accountService.findAll()).thenReturn(accounts);

        this.mockMvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].person").value("Andrey"))
                .andExpect(jsonPath("$[1].person").value("Jon"))
                .andExpect(jsonPath("$[0].balance").value("1000"))
                .andExpect(jsonPath("$[1].balance").value("2000"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(this.objectMapper.writeValueAsString(accounts)));

        verify(this.accountService).findAll();

    }

    @Test
    void save_test() throws Exception {
        var account = Account.builder().person("Pep").balance(new BigDecimal("3000")).build();

        when(this.accountService.save(account)).then(invocationOnMock -> {
            Account c = invocationOnMock.getArgument(0);
            c.setId(3L);
            return c;
        });

        account.setId(3L);

        this.mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Pep"))
                .andExpect(jsonPath("$.balance").value("3000"))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(content().json(this.objectMapper.writeValueAsString(account)));

        verify(this.accountService).save(any());

    }
}
