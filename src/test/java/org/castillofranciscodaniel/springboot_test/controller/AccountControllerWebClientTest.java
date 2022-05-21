package org.castillofranciscodaniel.springboot_test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.castillofranciscodaniel.springboot_test.models.Account;
import org.castillofranciscodaniel.springboot_test.models.TransactionDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccountControllerWebClientTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @Order(1)
    void transfer() {

        // given
        TransactionDto transactionDto = TransactionDto.builder()
                .originAccountNumber(1L)
                .destinationAccountNumber(2L)
                .amount(new BigDecimal("100"))
                .bankId(1L)
                .build();

        // when
        this.webTestClient.post().uri("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transactionDto)
                .exchange()

                // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(resp -> {
                    try {
                        JsonNode jsonNode = this.objectMapper.readTree(resp.getResponseBody());
                        assertEquals("Transfer ok!", jsonNode.path("message").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Transfer ok!"))
                .jsonPath("$.message").value(value -> assertEquals("Transfer ok!", value))
                .jsonPath("$.transaction.originAccountNumber").isEqualTo(transactionDto.getOriginAccountNumber());
    }

    @Test
    @Order(2)
    void findById() throws JsonProcessingException {
        Account account = Account.builder()
                .id(1L)
                .person("Andrey")
                .balance(new BigDecimal("900"))
                .build();

        this.webTestClient.get().uri("/api/accounts/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.person").isEqualTo("Andrey")
                .jsonPath("$.balance").isEqualTo(900)
                .json(this.objectMapper.writeValueAsString(account));
    }

    @Test
    @Order(3)
    void findById2() {
        this.webTestClient.get().uri("/api/accounts/2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(value -> {
                    Account account = value.getResponseBody();
                    assertEquals(account.getPerson(), "Jon");
                    assertEquals(account.getBalance().toPlainString(), "2100.00");
                });
    }


    @Test
    void list() {

    }


}