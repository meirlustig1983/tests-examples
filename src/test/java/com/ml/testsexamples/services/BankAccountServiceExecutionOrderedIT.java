package com.ml.testsexamples.services;

import com.ml.testsexamples.dto.BankAccountDto;
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
        Optional<BankAccountDto> result = service.getAccountInfo("john.doe@gmail.com");
        assertTrue(result.isPresent());
        assertThat(result.get().accountId()).isEqualTo("john.doe@gmail.com");
        assertThat(result.get().firstName()).isEqualTo("John");
        assertThat(result.get().lastName()).isEqualTo("Doe");
        assertThat(result.get().balance().doubleValue()).isEqualTo(2000);
        assertThat(result.get().minimumBalance().doubleValue()).isEqualTo(500);
        assertThat(result.get().active()).isEqualTo(true);
    }

    @Test
    @Order(2)
    public void makeDeposit_MakeDepositFor500_BalanceChangedTo2500() {
        Optional<BankAccountDto> result = service.makeDeposit("john.doe@gmail.com", 500);
        assertTrue(result.isPresent());
        assertThat(result.get().accountId()).isEqualTo("john.doe@gmail.com");
        assertThat(result.get().firstName()).isEqualTo("John");
        assertThat(result.get().lastName()).isEqualTo("Doe");
        assertThat(result.get().balance().doubleValue()).isEqualTo(2500);
        assertThat(result.get().minimumBalance().doubleValue()).isEqualTo(500);
        assertThat(result.get().active()).isEqualTo(true);
    }

    @Test
    @Order(3)
    public void makeDeposit_MakeDepositFor500_BalanceChangedTo3000() {
        Optional<BankAccountDto> result = service.makeDeposit("john.doe@gmail.com", 500);
        assertTrue(result.isPresent());
        assertThat(result.get().accountId()).isEqualTo("john.doe@gmail.com");
        assertThat(result.get().firstName()).isEqualTo("John");
        assertThat(result.get().lastName()).isEqualTo("Doe");
        assertThat(result.get().balance().doubleValue()).isEqualTo(3000);
        assertThat(result.get().minimumBalance().doubleValue()).isEqualTo(500);
        assertThat(result.get().active()).isEqualTo(true);
    }

    @Test
    @Order(4)
    public void makeWithdraw_MakeWithdrawFor2500_BalanceChangedTo500() {
        Optional<BankAccountDto> result = service.makeWithdraw("john.doe@gmail.com", 2500);
        assertTrue(result.isPresent());
        assertThat(result.get().accountId()).isEqualTo("john.doe@gmail.com");
        assertThat(result.get().firstName()).isEqualTo("John");
        assertThat(result.get().lastName()).isEqualTo("Doe");
        assertThat(result.get().balance().doubleValue()).isEqualTo(500);
        assertThat(result.get().minimumBalance().doubleValue()).isEqualTo(500);
        assertThat(result.get().active()).isEqualTo(true);
    }

    @Test
    @Order(5)
    public void makeWithdraw_MakeWithdrawFor1500_ThrowsInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class, () -> service.makeWithdraw("john.doe@gmail.com", 1500));
    }
}