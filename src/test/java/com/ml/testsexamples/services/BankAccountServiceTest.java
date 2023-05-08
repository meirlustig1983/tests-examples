package com.ml.testsexamples.services;

import com.ml.testsexamples.dto.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.repositories.BankAccountRepository;
import com.ml.testsexamples.utils.CustomDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MockitoSettings
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private BankAccountService service;

    @Test
    public void getAll() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();

        BankAccount bankAccount1 = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        BankAccount bankAccount2 = builder
                .id(2L)
                .firstName("Franklin")
                .lastName("Benjamin")
                .balance(BigDecimal.valueOf(0))
                .minimumBalance(BigDecimal.valueOf(-1000))
                .build();

        when(repository.findAll()).thenReturn(List.of(bankAccount1, bankAccount2));

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

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findById() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount bankAccount1 = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(bankAccount1));

        Optional<BankAccount> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(3500, result.get().getBalance().intValue());
        assertEquals(1500, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findById_FindBankAccountForNotExistsAccountId_EmptyOptional() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<BankAccount> result = service.findById(1L);

        assertFalse(result.isPresent());

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void update() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();

        BankAccount bankAccount = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        BankAccount updatedBankAccount = builder
                .id(1L)
                .firstName("Meir")
                .lastName("Roth")
                .balance(BigDecimal.valueOf(10000))
                .minimumBalance(BigDecimal.valueOf(0))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(bankAccount));
        when(repository.save(any(BankAccount.class))).thenReturn(updatedBankAccount);

        Optional<BankAccount> result = service.update(1L, List.of(Pair.of(BankAccountFields.FIRST_NAME, "Meir"),
                Pair.of(BankAccountFields.LAST_NAME, "Roth"), Pair.of(BankAccountFields.BALANCE, "10000"),
                Pair.of(BankAccountFields.MINIMUM_BALANCE, "0")));

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Meir", result.get().getFirstName());
        assertEquals("Roth", result.get().getLastName());
        assertEquals(10000, result.get().getBalance().intValue());
        assertEquals(0, result.get().getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get().getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get().getUpdatedAt());

        verify(repository).findById(1L);
        verify(repository).save(any(BankAccount.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void update_TryToUpdateBalanceFieldForNotExistsBankAccount_EmptyOptional() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        Optional<BankAccount> result = service.update(3L, List.of(Pair.of(BankAccountFields.BALANCE, "1000"), Pair.of(BankAccountFields.BALANCE, "15000")));
        assertFalse(result.isPresent());
    }

    @Test
    public void update_TryToUpdateUnauthorizedField_IllegalArgumentException() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();

        BankAccount originalDto = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(originalDto));

        assertThrows(IllegalArgumentException.class, () -> service.update(1L, List.of(Pair.of(BankAccountFields.ID, "1000"), Pair.of(BankAccountFields.BALANCE, "8500"))));

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }
}