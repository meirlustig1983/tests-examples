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

import java.time.LocalDateTime;
import java.util.Optional;

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
    @Sql(scripts = "/data/recreate-datasets.sql")
    public void deposit() {

        Optional<BankAccount> result = manager.deposit(1L, 50);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(3550, result.get().getBalance().intValue());
        assertEquals(1500, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());
    }

    @Test
    @DisplayName("Test deposit to a not-exists customer. Customer[id = 3], result=EntityNotFoundException")
    public void deposit_WithNotExistsCustomer() {
        assertThrows(EntityNotFoundException.class, () -> manager.deposit(3L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a customer. Customer[id = 1]")
    public void withdraw() {

        Optional<BankAccount> result = manager.withdraw(1L, 2000);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(1500, result.get().getBalance().intValue());
        assertEquals(1500, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());
    }

    @Test
    @DisplayName("Test withdraw from a customer until he run-out of his money. Customer[id = 1]")
    public void withdraw_FewTimes() {

        Optional<BankAccount> result = manager.withdraw(1L, 1000);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(2500, result.get().getBalance().intValue());
        assertEquals(1500, result.get().getMinimumBalance().intValue());

        LocalDateTime createdAt = result.get().getCreatedAt();
        LocalDateTime updatedAt = result.get().getUpdatedAt();

        assertInstanceOf(LocalDateTime.class, createdAt);
        assertInstanceOf(LocalDateTime.class, updatedAt);

        result = manager.withdraw(1L, 1000);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(1500, result.get().getBalance().intValue());
        assertEquals(1500, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());

        assertEquals(createdAt, result.get().getCreatedAt());
        assertNotEquals(updatedAt, result.get().getUpdatedAt());

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

}