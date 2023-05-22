package com.ml.testsexamples.mappers;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dto.BankAccountDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountMapperTest {

    @Test
    void toDto() {

        BankAccount.BankAccountBuilder builder = BankAccount.builder();
        BankAccount bankAccount = builder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(4500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccountDto bankAccountDto = BankAccountMapper.INSTANCE.toDto(bankAccount);

        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance()).isEqualTo(BigDecimal.valueOf(4500));
        assertThat(bankAccountDto.minimumBalance()).isEqualTo(BigDecimal.valueOf(1500));
        assertThat(bankAccountDto.active()).isTrue();
    }

    @Test
    void toDao() {

        BankAccountDto bankAccountDto = new BankAccountDto("Theodore", "Roosevelt",
                BigDecimal.valueOf(3000), BigDecimal.valueOf(1000), false);

        BankAccount bankAccount = BankAccountMapper.INSTANCE.toDao(bankAccountDto);

        assertThat(bankAccount.getId()).isNull();
        assertThat(bankAccount.getFirstName()).isEqualTo("Theodore");
        assertThat(bankAccount.getLastName()).isEqualTo("Roosevelt");
        assertThat(bankAccount.getBalance()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(bankAccount.getMinimumBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(bankAccount.isActive()).isFalse();
        assertThat(bankAccount.getUpdatedAt()).isNotNull();
        assertThat(bankAccount.getCreatedAt()).isNotNull();
    }
}