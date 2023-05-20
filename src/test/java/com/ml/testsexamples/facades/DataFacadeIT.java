package com.ml.testsexamples.facades;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.utils.CustomDisplayNameGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
@Transactional
@Sql(scripts = "/data/recreate-datasets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Timeout(value = 5)
public class DataFacadeIT {

    @Autowired
    private DataFacade dataFacade;

    @Test
    public void findAllBankAccounts() {

        List<BankAccount> result = dataFacade.findAllBankAccounts();

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
    public void findBankAccountById_TryToFindBankAccountForFirstAccountId_DataSuccessfullyReceived() {

        Optional<BankAccount> result = dataFacade.findBankAccountById(1L);

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
    public void findBankAccountById_TryToFindBankAccountForSecondAccountId_DataSuccessfullyReceived() {

        Optional<BankAccount> result = dataFacade.findBankAccountById(2L);

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
    public void saveBankAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount bankAccount = builder
                .firstName("Meir")
                .lastName("Lustig")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        Optional<BankAccount> result = dataFacade.saveBankAccount(bankAccount);

        assertTrue(result.isPresent());
        assertEquals("Meir", result.get().getFirstName());
        assertEquals("Lustig", result.get().getLastName());
        assertEquals(3500, result.get().getBalance().intValue());
        assertEquals(1500, result.get().getMinimumBalance().intValue());
        assertTrue(result.get().isActive());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());
    }

    @Test
    public void findBankAccountById_TryToFindBankAccountForNotExistsAccountId_EmptyOptional() {
        Optional<BankAccount> result = dataFacade.findBankAccountById(3L);
        assertFalse(result.isPresent());
    }

    @Test
    public void updateBankAccount() {

        Optional<BankAccount> result = dataFacade.findBankAccountById(2L);

        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Franklin", result.get().getFirstName());
        assertEquals("Benjamin", result.get().getLastName());
        assertEquals(0, result.get().getBalance().intValue());
        assertEquals(-1000, result.get().getMinimumBalance().intValue());
        assertFalse(result.get().isActive());

        LocalDateTime createdAt = result.get().getCreatedAt();
        LocalDateTime updatedAt = result.get().getUpdatedAt();

        assertInstanceOf(LocalDateTime.class, createdAt);
        assertInstanceOf(LocalDateTime.class, updatedAt);

        result = dataFacade.updateBankAccount(2L, List.of(Pair.of(BankAccountFields.FIRST_NAME, "Meir"),
                Pair.of(BankAccountFields.LAST_NAME, "Roth"), Pair.of(BankAccountFields.BALANCE, "10000"),
                Pair.of(BankAccountFields.MINIMUM_BALANCE, "0"), Pair.of(BankAccountFields.ACTIVE, "true")));

        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Meir", result.get().getFirstName());
        assertEquals("Roth", result.get().getLastName());
        assertEquals(10000, result.get().getBalance().intValue());
        assertEquals(0, result.get().getMinimumBalance().intValue());
        assertTrue(result.get().isActive());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());

        assertEquals(createdAt, result.get().getCreatedAt());
        assertNotEquals(updatedAt, result.get().getUpdatedAt());
    }

    @Test
    public void updateBankAccount_TryToUpdateUnauthorizedField_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> dataFacade.updateBankAccount(1L, List.of(Pair.of(BankAccountFields.ID, "1000"), Pair.of(BankAccountFields.BALANCE, "8500"))));
    }

    @Test
    public void updateBankAccount_TryToUpdateBalanceFieldForNotExistsAccountId_EmptyOptional() {
        Optional<BankAccount> result = dataFacade.updateBankAccount(3L, List.of(Pair.of(BankAccountFields.BALANCE, "1000"), Pair.of(BankAccountFields.BALANCE, "15000")));
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteBankAccountById() {

        List<BankAccount> result = dataFacade.findAllBankAccounts();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        dataFacade.deleteBankAccountById(2L);

        result = dataFacade.findAllBankAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}