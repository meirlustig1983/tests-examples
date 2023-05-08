package com.ml.testsexamples.managers;

import com.ml.testsexamples.dto.BankAccount;
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
import static org.mockito.Mockito.when;

@MockitoSettings
public class BankManagerTest {

    @Mock
    private BankAccountService service;

    @InjectMocks
    private BankManager manager;

    @Test
    @DisplayName("Test deposit to customer. Customer[id = 1]")
    public void deposit() {

        BankAccount.BankAccountBuilder customerDataBuilder = BankAccount.builder();
        BankAccount original = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        BankAccount updated = customerDataBuilder
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
    @DisplayName("Test deposit to not-exists customer. Customer[id = 3], result=EntityNotFoundException")
    public void deposit_WithNotExistsCustomer() {
        when(service.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> manager.deposit(3L, 50));
    }


    @Test
    @DisplayName("Test withdraw from a customer. Customer[id = 1]")
    public void withdraw() {

        BankAccount.BankAccountBuilder customerDataBuilder = BankAccount.builder();
        BankAccount original = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .build();

        BankAccount updated = customerDataBuilder
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
    @DisplayName("Test withdraw from not-exists customer. Customer[id = 3], result=EntityNotFoundException")
    public void withdraw_WithNotExistsCustomer() {
        when(service.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> manager.withdraw(3L, 50));
    }

    @Test
    @DisplayName("Test a withdraw from a customer with not enough money in his account. Customer[id = 1], result=InsufficientFundsException")
    public void withdraw_WithNInsufficientFundsException() {
        BankAccount.BankAccountBuilder customerDataBuilder = BankAccount.builder();
        BankAccount original = customerDataBuilder
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