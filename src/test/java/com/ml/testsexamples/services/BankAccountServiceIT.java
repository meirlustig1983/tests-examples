package com.ml.testsexamples.services;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.utils.CustomDisplayNameGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
@Transactional
@Sql(scripts = "/data/recreate-datasets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankAccountServiceIT {

    @Autowired
    private BankAccountService service;

    @Test
    public void getAll() {

        List<BankAccount> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Theodore", result.get(0).getFirstName());
        assertEquals("Roosevelt", result.get(0).getLastName());
        assertEquals(3500, result.get(0).getBalance().intValue());
        assertEquals(1500, result.get(0).getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get(0).getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get(0).getUpdatedAt());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Franklin", result.get(1).getFirstName());
        assertEquals("Benjamin", result.get(1).getLastName());
        assertEquals(0, result.get(1).getBalance().intValue());
        assertEquals(-1000, result.get(1).getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get(1).getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get(1).getUpdatedAt());
    }

    @Test
    public void findById_TryToFindBankAccountForFirstAccountId_DataSuccessfullyReceived() {

        Optional<BankAccount> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(3500, result.get().getBalance().intValue());
        assertEquals(1500, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());
    }

    @Test
    public void findById_TryToFindBankAccountForSecondAccountId_DataSuccessfullyReceived() {

        Optional<BankAccount> result = service.findById(2L);

        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Franklin", result.get().getFirstName());
        assertEquals("Benjamin", result.get().getLastName());
        assertEquals(0, result.get().getBalance().intValue());
        assertEquals(-1000, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());
    }

    @Test
    public void findById_TryToFindBankAccountForNotExistsAccountId_EmptyOptional() {
        Optional<BankAccount> result = service.findById(3L);
        assertFalse(result.isPresent());
    }

    @Test
    public void update() {

        Optional<BankAccount> result = service.findById(2L);

        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Franklin", result.get().getFirstName());
        assertEquals("Benjamin", result.get().getLastName());
        assertEquals(0, result.get().getBalance().intValue());
        assertEquals(-1000, result.get().getMinimumBalance().intValue());

        LocalDateTime createdAt = result.get().getCreatedAt();
        LocalDateTime updatedAt = result.get().getUpdatedAt();

        assertInstanceOf(LocalDateTime.class, createdAt);
        assertInstanceOf(LocalDateTime.class, updatedAt);

        result = service.update(2L, List.of(Pair.of(BankAccountFields.FIRST_NAME, "Meir"),
                Pair.of(BankAccountFields.LAST_NAME, "Roth"), Pair.of(BankAccountFields.BALANCE, "10000"),
                Pair.of(BankAccountFields.MINIMUM_BALANCE, "0")));

        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Meir", result.get().getFirstName());
        assertEquals("Roth", result.get().getLastName());
        assertEquals(10000, result.get().getBalance().intValue());
        assertEquals(0, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());

        assertEquals(createdAt, result.get().getCreatedAt());
        assertNotEquals(updatedAt, result.get().getUpdatedAt());
    }

    @Test
    public void update_TryToUpdateUnauthorizedField_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, List.of(Pair.of(BankAccountFields.ID, "1000"), Pair.of(BankAccountFields.BALANCE, "8500"))));
    }

    @Test
    public void update_TryToUpdateBalanceFieldForNotExistsAccountId_EmptyOptional() {
        Optional<BankAccount> result = service.update(3L, List.of(Pair.of(BankAccountFields.BALANCE, "1000"), Pair.of(BankAccountFields.BALANCE, "15000")));
        assertFalse(result.isPresent());
    }
}