package com.ml.testsexamples.managers;

import com.ml.testsexamples.dto.BankAccount;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Transactional
@Sql(scripts = "/data/recreate-datasets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankManagerIT {

    @Autowired
    private BankManager manager;

    @Test
    @DisplayName("Test deposit to a customer. Customer[id = 1]")
    public void deposit() {

        Optional<BankAccount> result = manager.deposit(1L, 50);

        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3550);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @DisplayName("Test deposit to a customer and measure time. Customer[id = 1]")
    public void deposit_measureTime() {
        assertTimeout(Duration.ofMillis(50), () -> manager.deposit(1L, 50));
    }

    @Test
    @DisplayName("Test deposit to a not-exists customer. Customer[id = 3], result=EntityNotFoundException")
    public void deposit_WithNotExistsCustomer() {
        assertThrows(EntityNotFoundException.class, () -> manager.deposit(3L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a customer. Customer[id = 1]")
    public void withdraw() {

        Optional<BankAccount> result = manager.withdraw(1L, 1999);

        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(1501);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @DisplayName("Test withdraw from a customer and measure time. Customer[id = 1]")
    public void withdraw_measureTime() {
        assertTimeout(Duration.ofMillis(50), () -> manager.withdraw(1L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a customer until he run-out of his money. Customer[id = 1]")
    public void withdraw_BelowMinimum() {

        Optional<BankAccount> result = manager.withdraw(1L, 1000);

        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(2500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());

        LocalDateTime createdAt = result.get().getCreatedAt();
        LocalDateTime updatedAt = result.get().getUpdatedAt();

        assertInstanceOf(LocalDateTime.class, createdAt);
        assertInstanceOf(LocalDateTime.class, updatedAt);

        result = manager.withdraw(1L, 1000);

        assertTrue(result.isPresent());
        bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance().doubleValue()).isEqualTo(bankAccount.getMinimumBalance().doubleValue());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

        assertThat(result.get().getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.get().getUpdatedAt()).isNotEqualTo(createdAt);
        assertThat(result.get().getUpdatedAt()).isNotEqualTo(updatedAt);

        assertThrows(InsufficientFundsException.class, () -> manager.withdraw(1L, 1000));
    }

    @Test
    @DisplayName("Test withdraw from a not-exists customer. Customer[id = 3], result=EntityNotFoundException")
    public void withdraw_WithNotExistsCustomer() {
        assertThrows(EntityNotFoundException.class, () -> manager.withdraw(3L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a customer with not enough money in his account. Customer[id = 1], result=InsufficientFundsException")
    public void withdraw_WithNInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class, () -> manager.withdraw(1L, 2001));
    }

    @Test
    @DisplayName("Test withdraw and deposit a few times for the same bank account. Customer[id = 1], result=InsufficientFundsException")
    public void withdraw_deposit() {

        assertAll(() -> manager.withdraw(1L, 100), () -> manager.withdraw(1L, 100),
                () -> manager.withdraw(1L, 100), () -> manager.deposit(1L, 1000),
                () -> manager.withdraw(1L, 100), () -> manager.withdraw(1L, 100));

        Optional<BankAccount> result = manager.withdraw(1L, 1);

        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3999);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }
}