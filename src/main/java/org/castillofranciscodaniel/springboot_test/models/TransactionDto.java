package org.castillofranciscodaniel.springboot_test.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private Long originAccountNumber;
    private Long destinationAccountNumber;
    private BigDecimal amount;
    private Long bankId;
}
