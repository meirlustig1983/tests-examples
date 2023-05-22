package com.ml.testsexamples.services;

import com.ml.testsexamples.dto.BankAccountDto;
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

        Optional<BankAccountDto> result = service.getAccountInfo(1L);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();

        assumingThat(bankAccountDto.active(), () -> assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance()));
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
    }

    @Test
    @DisplayName("Test get info about bank account, if (OS is LINUX). BankAccount[id = 1]")
    @Timeout(value = 10000, unit = TimeUnit.MICROSECONDS)
    @EnabledOnOs({OS.LINUX})
    public void getAccountInfo_EnabledOnOsLINUX() {

        Optional<BankAccountDto> result = service.getAccountInfo(1L);
        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();

        assumingThat(bankAccountDto.active(), () -> assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance()));
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
    }

    @Test
    @DisplayName("Test create and delete bank account. BankAccount[id = 1]")
    @Sql(scripts = "/data/recreate-datasets-0.sql")
    public void createAccount() {

        BankAccountDto newBankAccountDto = new BankAccountDto("Meir", "Lustig",
                BigDecimal.valueOf(1_000_000), BigDecimal.valueOf(1_500), true);

        Optional<BankAccountDto> result = service.createAccount(newBankAccountDto);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();

        assertThat(bankAccountDto.firstName()).isEqualTo("Meir");
        assertThat(bankAccountDto.lastName()).isEqualTo("Lustig");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(1_000_000);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1_500);
        assertThat(bankAccountDto.active()).isTrue();
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        service.deleteBankAccountById(1L);

        assertThrows(EntityNotFoundException.class, () -> service.getAccountInfo(1L));
    }

    @Test
    @DisplayName("Test activate bank account. BankAccount[id = 2]")
    public void activateAccount() {

        Optional<BankAccountDto> result = service.getAccountInfo(2L);

        assertThat(result.isPresent()).isTrue();

        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isFalse();

        result = service.activateAccount(2L);

        assertThat(result.isPresent()).isTrue();

        bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isTrue();
    }

    @Test
    @DisplayName("Test activate not-exists bank account. BankAccount[id = 3]")
    public void activateAccount_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.activateAccount(3L));
    }

    @Test
    @DisplayName("Test deactivate bank account. BankAccount[id = 1]")
    public void deactivateAccount() {

        Optional<BankAccountDto> result = service.getAccountInfo(1L);

        assertThat(result.isPresent()).isTrue();

        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isTrue();

        result = service.deactivateAccount(1L);

        assertThat(result.isPresent()).isTrue();

        bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isFalse();
    }

    @Test
    @DisplayName("Test deactivate not-exists bank account. BankAccount[id = 1]")
    public void deactivateAccount_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.deactivateAccount(3L));
    }

    @Test
    @DisplayName("Test deposit to a bank account. BankAccount[id = 1]")
    public void makeDeposit() {

        Optional<BankAccountDto> result = service.makeDeposit(1L, 50);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3550);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());
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

        Optional<BankAccountDto> result = service.makeWithdraw(1L, 1999);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(1501);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());
    }

    @Test
    @DisplayName("Test withdraw from a bank account and measure time. BankAccount[id = 1]")
    public void makeWithdraw_measureTime() {
        assertTimeout(Duration.ofMillis(70), () -> service.makeWithdraw(1L, 50));
    }

    @Test
    @DisplayName("Test withdraw from a bank account until it run-out of the money. BankAccount[id = 1]")
    public void makeWithdraw_BelowMinimum() {

        Optional<BankAccountDto> result = service.makeWithdraw(1L, 1000);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(2500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        result = service.makeWithdraw(1L, 1000);

        assertTrue(result.isPresent());
        bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance().doubleValue()).isEqualTo(bankAccountDto.minimumBalance().doubleValue());

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

        Optional<BankAccountDto> result = service.makeWithdraw(1L, 1);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3999);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());
    }
}