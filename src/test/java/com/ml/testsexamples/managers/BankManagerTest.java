package com.ml.testsexamples.managers;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import com.ml.testsexamples.services.BankAccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.when;

@MockitoSettings
public class BankManagerTest {

    @Mock
    private BankAccountService service;

    @InjectMocks
    private BankManager manager;

    @Test
    @DisplayName("Test get an info about bank account and run some tests it the account is active. BankAccount[id = 1]")
    public void info_GetInfoForFirstAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));

        Optional<BankAccount> result = manager.info(1L);

        assertTrue(result.isPresent());
        assumeTrue(result.get().isActive(), "The account is not active");
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @DisplayName("Test get an info about bank account and run some tests it the account is active. BankAccount[id = 1]")
    public void info_GetInfoForSecondAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(2L)
                .firstName("Franklin")
                .lastName("Benjamin")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));

        Optional<BankAccount> result = manager.info(1L);

        assertTrue(result.isPresent());
        assumeTrue(result.get().isActive(), "The account is not active");
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(2L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Franklin");
        assertThat(bankAccount.getLastName()).isEqualTo("Benjamin");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(0);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(-1000);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @DisplayName("Test deposit to bank account. BankAccount[id = 1]")
    public void deposit() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        BankAccount updated = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3550))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));
        when(service.update(1L, List.of(Pair.of(BankAccountFields.BALANCE, "3550.0")))).thenReturn(Optional.of(updated));

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
    @DisplayName("Test deposit to not-exists bank account. BankAccount[id = 3], result=EntityNotFoundException")
    public void deposit_WithNotExistsBankAccount() {
        when(service.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> manager.deposit(3L, 50));
    }


    @Test
    @DisplayName("Test withdraw from a bank account. BankAccount[id = 1]")
    public void withdraw() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        BankAccount updated = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(1501))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));
        when(service.update(1L, List.of(Pair.of(BankAccountFields.BALANCE, "1501.0")))).thenReturn(Optional.of(updated));

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
    @DisplayName("Test withdraw from not-exists bank account. BankAccount[id = 3], result=EntityNotFoundException")
    public void withdraw_WithNotExistsBankAccount() {
        when(service.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> manager.withdraw(3L, 50));
    }

    @Test
    @DisplayName("Test a withdraw from a bank account with not enough money. BankAccount[id = 1], result=InsufficientFundsException")
    public void withdraw_WithNInsufficientFundsException() {
        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));
        assertThrows(InsufficientFundsException.class, () -> manager.withdraw(1L, 2001));
    }
}