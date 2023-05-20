package com.ml.testsexamples.services;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.exceptions.InactiveAccountException;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import com.ml.testsexamples.facades.DataFacade;
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
import static org.mockito.Mockito.*;

@MockitoSettings
public class BankAccountServiceTest {

    @Mock
    private DataFacade dataFacade;

    @InjectMocks
    private BankAccountService service;

    @Test
    @DisplayName("Test get an info about bank account and run some tests it the account is active. BankAccount[id = 1]")
    public void getAccountInfo_GetInfoForBankAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(original));

        Optional<BankAccount> result = service.getAccountInfo(1L);

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

        verify(dataFacade).findBankAccountById(1L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test get an info about bank account for invalid bank account. BankAccount[id = 3]")
    public void getAccountInfo_GetInfoForInvalidBankAccount() {
        when(dataFacade.findBankAccountById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getAccountInfo(3L));

        verify(dataFacade).findBankAccountById(3L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test create new bank account. BankAccount[id = 3]")
    public void createAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(3L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(dataFacade.saveBankAccount(original)).thenReturn(Optional.of(original));

        Optional<BankAccount> result = service.createAccount(original);

        assertTrue(result.isPresent());
        assumeTrue(result.get().isActive(), "The account is not active");
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(3L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

        verify(dataFacade).saveBankAccount(original);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test delete bank account by id. BankAccount[id = 3]")
    public void deleteBankAccountById() {
        service.deleteBankAccountById(3L);
        verify(dataFacade).deleteBankAccountById(3L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test activate bank account. BankAccount[id = 1]")
    public void activateAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        BankAccount updated = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount(1L, List.of(Pair.of(BankAccountFields.ACTIVE, "true")))).thenReturn(Optional.of(updated));

        Optional<BankAccount> result = service.activateAccount(1L);

        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.isActive()).isTrue();
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

        verify(dataFacade).findBankAccountById(1L);
        verify(dataFacade).updateBankAccount(1L, List.of(Pair.of(BankAccountFields.ACTIVE, "true")));
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test activate on to not-exists bank account. BankAccount[id = 1]")
    public void activateAccount_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.activateAccount(1L));
        verify(dataFacade).findBankAccountById(1L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deactivate bank account. BankAccount[id = 1]")
    public void deactivateAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccount updated = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount(1L, List.of(Pair.of(BankAccountFields.ACTIVE, "false")))).thenReturn(Optional.of(updated));

        Optional<BankAccount> result = service.deactivateAccount(1L);

        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();
        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.isActive()).isFalse();
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

        verify(dataFacade).findBankAccountById(1L);
        verify(dataFacade).updateBankAccount(1L, List.of(Pair.of(BankAccountFields.ACTIVE, "false")));
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deactivate on to not-exists bank account. BankAccount[id = 1]")
    public void deactivateAccount_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.deactivateAccount(1L));
        verify(dataFacade).findBankAccountById(1L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deposit to bank account. BankAccount[id = 1]")
    public void makeDeposit() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccount updated = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3550))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount(1L, List.of(Pair.of(BankAccountFields.BALANCE, "3550.0")))).thenReturn(Optional.of(updated));

        Optional<BankAccount> result = service.makeDeposit(1L, 50);

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

        verify(dataFacade).findBankAccountById(1L);
        verify(dataFacade).updateBankAccount(1L, List.of(Pair.of(BankAccountFields.BALANCE, "3550.0")));
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deposit to not-exists bank account. BankAccount[id = 3], result=EntityNotFoundException")
    public void makeDeposit_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.makeDeposit(3L, 50));
        verify(dataFacade).findBankAccountById(3L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deposit to inactive bank account. BankAccount[id = 1], result=InactiveAccountException")
    public void makeDeposit_WithInactiveAccount() {
        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount bankAccount = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(bankAccount));
        assertThrows(InactiveAccountException.class, () -> service.makeDeposit(1L, 50));
        verify(dataFacade).findBankAccountById(1L);
        verifyNoMoreInteractions(dataFacade);
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
                .active(true)
                .build();

        BankAccount updated = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(1501))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount(1L, List.of(Pair.of(BankAccountFields.BALANCE, "1501.0")))).thenReturn(Optional.of(updated));

        Optional<BankAccount> result = service.makeWithdraw(1L, 1999);

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

        verify(dataFacade).findBankAccountById(1L);
        verify(dataFacade).updateBankAccount(1L, List.of(Pair.of(BankAccountFields.BALANCE, "1501.0")));
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test withdraw from not-exists bank account. BankAccount[id = 3], result=EntityNotFoundException")
    public void makeWithdraw_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.makeWithdraw(3L, 50));
        verify(dataFacade).findBankAccountById(3L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test withdraw from inactive bank account. BankAccount[id = 1], result=InactiveAccountException")
    public void makeWithdraw_WithInactiveAccount() {
        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount bankAccount = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(bankAccount));
        assertThrows(InactiveAccountException.class, () -> service.makeWithdraw(1L, 50));
        verify(dataFacade).findBankAccountById(1L);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test a withdraw from a bank account with not enough money. BankAccount[id = 1], result=InsufficientFundsException")
    public void makeWithdraw_WithNInsufficientFundsException() {
        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount original = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(dataFacade.findBankAccountById(1L)).thenReturn(Optional.of(original));
        assertThrows(InsufficientFundsException.class, () -> service.makeWithdraw(1L, 2001));

        verify(dataFacade).findBankAccountById(1L);
        verifyNoMoreInteractions(dataFacade);
    }
}