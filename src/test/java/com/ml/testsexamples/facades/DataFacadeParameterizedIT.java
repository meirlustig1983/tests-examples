package com.ml.testsexamples.facades;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.utils.CustomDisplayNameGenerator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(scripts = "/data/recreate-datasets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
public class DataFacadeParameterizedIT {

    @Autowired
    private DataFacade dataFacade;

    @ParameterizedTest
    @ValueSource(strings = {"100", "200", "400", "800"})
    public void updateBankAccount_TryToUpdateBalance_BalanceHasBeenUpdated(String balance) {
        Optional<BankAccount> result = dataFacade.updateBankAccount("theodore.roosevelt@gmail.com",
                List.of(Pair.of(BankAccountFields.BALANCE, balance)));
        assertTrue(result.isPresent());
        BankAccount bankAccountResult = result.get();
        assertThat(bankAccountResult.getId().intValue()).isEqualTo(1);
        assertThat(bankAccountResult.getBalance().intValue()).isEqualTo(Integer.parseInt(balance));
    }

    @ParameterizedTest
    @CsvSource({"100, 200", "400, 800"})
    public void updateBankAccount_TryToUpdateBalanceAndMinimumBalanced_BalanceAndMinimumHasBeenUpdated(String balance, String minimumBalance) {
        Optional<BankAccount> result = dataFacade.updateBankAccount("theodore.roosevelt@gmail.com",
                List.of(Pair.of(BankAccountFields.BALANCE, balance), Pair.of(BankAccountFields.MINIMUM_BALANCE, minimumBalance)));
        assertTrue(result.isPresent());
        BankAccount bankAccountResult = result.get();
        assertThat(bankAccountResult.getId().intValue()).isEqualTo(1);
        assertThat(bankAccountResult.getBalance().intValue()).isEqualTo(Integer.parseInt(balance));
        assertThat(bankAccountResult.getMinimumBalance().intValue()).isEqualTo(Integer.parseInt(minimumBalance));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/tests/tests-data.csv", delimiter = ',')
    public void updateBankAccount_TryToUpdateAllDataFieldsFromCsvFile_AllDataHasBeenUpdated(String firstName, String lastName, String balance, String minimumBalance) {
        Optional<BankAccount> result = dataFacade.updateBankAccount("theodore.roosevelt@gmail.com",
                List.of(Pair.of(BankAccountFields.FIRST_NAME, firstName),
                        Pair.of(BankAccountFields.LAST_NAME, lastName),
                        Pair.of(BankAccountFields.BALANCE, balance),
                        Pair.of(BankAccountFields.MINIMUM_BALANCE, minimumBalance)));

        assertTrue(result.isPresent());
        BankAccount bankAccountResult = result.get();
        assertThat(bankAccountResult.getId().intValue()).isEqualTo(1);
        assertThat(bankAccountResult.getFirstName()).isEqualTo(firstName);
        assertThat(bankAccountResult.getLastName()).isEqualTo(lastName);
        assertThat(bankAccountResult.getBalance().intValue()).isEqualTo(Integer.parseInt(balance));
        assertThat(bankAccountResult.getMinimumBalance().intValue()).isEqualTo(Integer.parseInt(minimumBalance));
    }
}
