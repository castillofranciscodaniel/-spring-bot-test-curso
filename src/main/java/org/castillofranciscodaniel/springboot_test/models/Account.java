package org.castillofranciscodaniel.springboot_test.models;

import lombok.*;
import org.castillofranciscodaniel.springboot_test.exceptions.InsufficientMoneyException;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String person;
    private BigDecimal balance;

    public void debit(BigDecimal amount) {
        var newBalance = this.balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientMoneyException("the account have insufficient Money");
        }
        this.balance = newBalance;
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
