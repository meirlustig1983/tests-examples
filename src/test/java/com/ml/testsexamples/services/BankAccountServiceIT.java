package com.ml.testsexamples.services;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@SpringBootTest
@Transactional
@Sql(scripts = "/data/recreate-datasets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankAccountServiceIT {

    @Autowired
    private BankAccountService service;

    @Test
    @DisplayName("Test get info about bank account, if (OS is MAC) & (JRE is JAVA_17). BankAccount[id = 1]")
    @Timeout(value = 3000, unit = TimeUnit.MICROSECONDS)
    @EnabledOnOs({OS.MAC})
    @EnabledOnJre({JRE.JAVA_17})
    public void getAccountInfo_EnabledOnOsMAC_EnabledOnJre17() {

        Optional<BankAccount> result = service.getAccountInfo(1L);

        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();

        assumingThat(bankAccount.isActive(), () -> assertThat(bankAccount.getId()).isEqualTo(1L));
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @DisplayName("Test get info about bank account, if (OS is LINUX). BankAccount[id = 1]")
    @Timeout(value = 10000, unit = TimeUnit.MICROSECONDS)
    @EnabledOnOs({OS.LINUX})
    public void getAccountInfo_EnabledOnOsLINUX() {

        Optional<BankAccount> result = service.getAccountInfo(1L);
        assertTrue(result.isPresent());
        BankAccount bankAccount = result.get();

        assumingThat(bankAccount.isActive(), () -> assertThat(bankAccount.getId()).isEqualTo(1L));
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance().intValue()).isEqualTo(3500);
        assertThat(bankAccount.getMinimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccount.getBalance()).isGreaterThan(bankAccount.getMinimumBalance());
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    @DisplayName("Test create and delete bank account. BankAccount[id = 1]")
    @Sql(scripts = "/data/recreate-datasets-0.sql")
    public void createAccount() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount newBankAccount = builder
                .firstName("Meir")
                .lastName("Lustig")
                .balance(BigDecimal.valueOf(1_000_000))
                .minimumBalance(BigDecimal.valueOf(1_500))
                .active(true)
                .build();

        Optional<BankAccount> result3 = service.createAccount(newBankAccount);

        assertTrue(result3.isPresent());
        BankAccount bankAccount3 = result3.get();

        assertThat(bankAccount3.getId()).isGreaterThanOrEqualTo(1);
        assertThat(bankAccount3.getFirstName()).isEqualTo("Meir");
        assertThat(bankAccount3.getLastName()).isEqualTo("Lustig");
        assertThat(bankAccount3.getBalance().intValue()).isEqualTo(1_000_000);
        assertThat(bankAccount3.getMinimumBalance().intValue()).isEqualTo(1_500);
        assertThat(bankAccount3.isActive()).isTrue();
        assertThat(bankAccount3.getBalance()).isGreaterThan(bankAccount3.getMinimumBalance());
        assertThat(bankAccount3.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount3.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

        service.deleteBankAccountById(1L);

        assertThrows(EntityNotFoundException.class, () -> service.getAccountInfo(1L));
    }

    @Test
    @DisplayName("Test activate bank account. BankAccount[id = 2]")
    public void activateAccount() {

        Optional<BankAccount> result = service.getAccountInfo(2L);

        assertThat(result.isPresent()).isTrue();

        BankAccount bankAccount = result.get();
        assertThat(bankAccount.isActive()).isFalse();

        result = service.activateAccount(2L);

        assertThat(result.isPresent()).isTrue();

        bankAccount = result.get();
        assertThat(bankAccount.isActive()).isTrue();
    }

    @Test
    @DisplayName("Test activate not-exists bank account. BankAccount[id = 3]")
    public void activateAccount_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.activateAccount(3L));
    }

    @Test
    @DisplayName("Test deactivate bank account. BankAccount[id = 1]")
    public void deactivateAccount() {

        Optional<BankAccount> result = service.getAccountInfo(1L);

        assertThat(result.isPresent()).isTrue();

        BankAccount bankAccount = result.get();
        assertThat(bankAccount.isActive()).isTrue();

        result = service.deactivateAccount(1L);

        assertThat(result.isPresent()).isTrue();

        bankAccount = result.get();
        assertThat(bankAccount.isActive()).isFalse();
    }

    @Test
    @DisplayName("Test deactivate not-exists bank account. BankAccount[id = 1]")
    public void deactivateAccount_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.deactivateAccount(3L));
    }

    @Test
    @DisplayName("Test deposit to a bank account. BankAccount[id = 1]")
    public void makeDeposit() {

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
    }

    @Test
    @DisplayName("Test deposit to a bank account and measure time. BankAccount[id = 1]")
    public void makeDeposit_measureTime() {
        assertTimeout(Duration.ofMillis(60), () -> service.makeDeposit(1L, 50));
    }

    @Test
    @DisplayName("Test deposit to a not-exists bank account. BankAccount[id = 3], result=EntityNotFoundException")
    public void makeDeposit_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.makeDeposit(3L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a bank account. BankAccount[id = 1]")
    public void makeWithdraw() {

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
    }

    @Test
    @DisplayName("Test withdraw from a bank account and measure time. BankAccount[id = 1]")
    public void makeWithdraw_measureTime() {
        assertTimeout(Duration.ofMillis(70), () -> service.makeWithdraw(1L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a bank account until it run-out of the money. BankAccount[id = 1]")
    public void makeWithdraw_BelowMinimum() {

        Optional<BankAccount> result = service.makeWithdraw(1L, 1000);

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

        result = service.makeWithdraw(1L, 1000);

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

        assertThrows(InsufficientFundsException.class, () -> service.makeWithdraw(1L, 1000));
    }

    @Test
    @DisplayName("Test withdraw from a not-exists bank account. BankAccount[id = 3], result=EntityNotFoundException")
    public void makeWithdraw_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.makeWithdraw(3L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a bank account with not enough money in his account. BankAccount[id = 1], result=InsufficientFundsException")
    public void makeWithdraw_WithNInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class, () -> service.makeWithdraw(1L, 2001));
    }

    @Test
    @DisplayName("Test withdraw and deposit a few times for the same bank account. BankAccount[id = 1], result=InsufficientFundsException")
    public void makeWithdraw_makeDeposit() {

        assertAll(() -> service.makeWithdraw(1L, 100), () -> service.makeWithdraw(1L, 100),
                () -> service.makeWithdraw(1L, 100), () -> service.makeDeposit(1L, 1000),
                () -> service.makeWithdraw(1L, 100), () -> service.makeWithdraw(1L, 100));

        Optional<BankAccount> result = service.makeWithdraw(1L, 1);

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