package com.ml.testsexamples.facades;

import com.ml.testsexamples.dao.BankAccount;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataFacadeExecutionOrderedTest {

    private static BankAccount bankAccount;

    @Test
    @Order(1)
    public void create_TryToCreateANewBankAccount_NewAccountDataSuccessfullyCreated() {
        bankAccount = BankAccount.builder()
                .id(1L)
                .accountId("meir.lustig@gmail.com")
                .firstName("Meir")
                .lastName("Lustig")
                .balance(BigDecimal.valueOf(5000))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        assertThat(bankAccount.getId()).isGreaterThanOrEqualTo(1L);
        assertThat(bankAccount.getAccountId()).isEqualTo("meir.lustig@gmail.com");
        assertThat(bankAccount.getFirstName()).isEqualTo("Meir");
        assertThat(bankAccount.getLastName()).isEqualTo("Lustig");
        assertThat(bankAccount.getBalance().doubleValue()).isEqualTo(5000);
        assertThat(bankAccount.getMinimumBalance().doubleValue()).isEqualTo(1500);
        assertThat(bankAccount.isActive()).isTrue();
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getCreatedAt()).isNotEqualTo(bankAccount.getUpdatedAt());
    }

    @Test
    @Order(2)
    public void update_TryToUpdateTheBankAccountBalance_DataSuccessfullyUpdated() {

        bankAccount.setBalance(BigDecimal.valueOf(6000));

        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getAccountId()).isEqualTo("meir.lustig@gmail.com");
        assertThat(bankAccount.getFirstName()).isEqualTo("Meir");
        assertThat(bankAccount.getLastName()).isEqualTo("Lustig");
        assertThat(bankAccount.getBalance().doubleValue()).isEqualTo(6000);
        assertThat(bankAccount.getMinimumBalance().doubleValue()).isEqualTo(1500);
        assertThat(bankAccount.isActive()).isTrue();
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getCreatedAt()).isNotEqualTo(bankAccount.getUpdatedAt());
    }

    @Test
    @Order(3)
    public void update_TryToUpdateTheBankAccountMinimumBalance_DataSuccessfullyUpdated() {

        bankAccount.setMinimumBalance(BigDecimal.valueOf(-1000));

        assertThat(bankAccount.getId()).isEqualTo(1L);
        assertThat(bankAccount.getAccountId()).isEqualTo("meir.lustig@gmail.com");
        assertThat(bankAccount.getFirstName()).isEqualTo("Meir");
        assertThat(bankAccount.getLastName()).isEqualTo("Lustig");
        assertThat(bankAccount.getBalance().doubleValue()).isEqualTo(6000);
        assertThat(bankAccount.getMinimumBalance().doubleValue()).isEqualTo(-1000);
        assertThat(bankAccount.isActive()).isTrue();
        assertThat(bankAccount.getCreatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
        assertThat(bankAccount.getCreatedAt()).isNotEqualTo(bankAccount.getUpdatedAt());
    }
}