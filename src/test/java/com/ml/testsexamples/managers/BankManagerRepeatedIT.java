package com.ml.testsexamples.managers;

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
public class BankManagerRepeatedIT {

    @Autowired
    private BankManager bankManager;

    @Test
    @Order(1)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/data/recreate-datasets-2.sql")
    public void info() {
        Optional<BankAccount> bankAccount = bankManager.info(1L);
        assertTrue(bankAccount.isPresent());
        assertThat(bankAccount.get().getFirstName()).isEqualTo("John");
        assertThat(bankAccount.get().getLastName()).isEqualTo("Doe");
        assertThat(bankAccount.get().getBalance().doubleValue()).isEqualTo(2000);
        assertThat(bankAccount.get().getMinimumBalance().doubleValue()).isEqualTo(500);
        assertThat(bankAccount.get().isActive()).isEqualTo(true);
    }

    @Order(2)
    @RepeatedTest(3)
    public void withdraw() {
        Optional<BankAccount> bankAccount = bankManager.withdraw(1L, 500);
        assertTrue(bankAccount.isPresent());
        assertThat(bankAccount.get().getFirstName()).isEqualTo("John");
        assertThat(bankAccount.get().getLastName()).isEqualTo("Doe");
        assertThat(bankAccount.get().getBalance().doubleValue()).isGreaterThanOrEqualTo(500);
        assertThat(bankAccount.get().getBalance().doubleValue()).isLessThan(2000);
        assertThat(bankAccount.get().getMinimumBalance().doubleValue()).isEqualTo(500);
        assertThat(bankAccount.get().isActive()).isEqualTo(true);
    }

    @Test
    @Order(3)
    public void deposit_MakeWithdrawFor1500_ThrowsInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class, () -> bankManager.withdraw(1L, 1500));
    }
}
