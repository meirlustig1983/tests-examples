package com.ml.testsexamples.services;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import com.ml.testsexamples.utils.CustomDisplayNameGenerator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankAccountServiceExecutionOrderedIT {

    @Autowired
    private BankAccountService service;

    @Test
    @Order(1)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/data/recreate-datasets-2.sql")
    public void getAccountInfo() {
        Optional<BankAccount> bankAccount = service.getAccountInfo(1L);
        assertTrue(bankAccount.isPresent());
        assertThat(bankAccount.get().getFirstName()).isEqualTo("John");
        assertThat(bankAccount.get().getLastName()).isEqualTo("Doe");
        assertThat(bankAccount.get().getBalance().doubleValue()).isEqualTo(2000);
        assertThat(bankAccount.get().getMinimumBalance().doubleValue()).isEqualTo(500);
        assertThat(bankAccount.get().isActive()).isEqualTo(true);
    }

    @Test
    @Order(2)
    public void makeDeposit_MakeDepositFor500_BalanceChangedTo2500() {
        Optional<BankAccount> bankAccount = service.makeDeposit(1L, 500);
        assertTrue(bankAccount.isPresent());
        assertThat(bankAccount.get().getFirstName()).isEqualTo("John");
        assertThat(bankAccount.get().getLastName()).isEqualTo("Doe");
        assertThat(bankAccount.get().getBalance().doubleValue()).isEqualTo(2500);
        assertThat(bankAccount.get().getMinimumBalance().doubleValue()).isEqualTo(500);
        assertThat(bankAccount.get().isActive()).isEqualTo(true);
    }

    @Test
    @Order(3)
    public void makeDeposit_MakeDepositFor500_BalanceChangedTo3000() {
        Optional<BankAccount> bankAccount = service.makeDeposit(1L, 500);
        assertTrue(bankAccount.isPresent());
        assertThat(bankAccount.get().getFirstName()).isEqualTo("John");
        assertThat(bankAccount.get().getLastName()).isEqualTo("Doe");
        assertThat(bankAccount.get().getBalance().doubleValue()).isEqualTo(3000);
        assertThat(bankAccount.get().getMinimumBalance().doubleValue()).isEqualTo(500);
        assertThat(bankAccount.get().isActive()).isEqualTo(true);
    }

    @Test
    @Order(4)
    public void makeWithdraw_MakeWithdrawFor2500_BalanceChangedTo500() {
        Optional<BankAccount> bankAccount = service.makeWithdraw(1L, 2500);
        assertTrue(bankAccount.isPresent());
        assertThat(bankAccount.get().getFirstName()).isEqualTo("John");
        assertThat(bankAccount.get().getLastName()).isEqualTo("Doe");
        assertThat(bankAccount.get().getBalance().doubleValue()).isEqualTo(500);
        assertThat(bankAccount.get().getMinimumBalance().doubleValue()).isEqualTo(500);
        assertThat(bankAccount.get().isActive()).isEqualTo(true);
    }

    @Test
    @Order(5)
    public void makeWithdraw_MakeWithdrawFor1500_ThrowsInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class, () -> service.makeWithdraw(1L, 1500));
    }
}