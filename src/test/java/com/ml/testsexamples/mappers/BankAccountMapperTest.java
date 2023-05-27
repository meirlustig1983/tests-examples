package com.ml.testsexamples.mappers;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dao.Transaction;
import com.ml.testsexamples.dto.BankAccountDto;
import com.ml.testsexamples.dto.TransactionDto;
import com.ml.testsexamples.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountMapperTest {

    @Test
    void toDto() {

        Transaction transaction = Transaction.builder()
                .id(1L)
                .bankAccountId(1L)
                .amount(BigDecimal.valueOf(4500))
                .type(TransactionType.DEPOSIT)
                .build();

        BankAccount bankAccount = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(4500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .transactions(List.of(transaction))
                .build();

        BankAccountDto bankAccountDto = BankAccountMapper.INSTANCE.toDto(bankAccount);
        TransactionDto transactionDto = bankAccountDto.transactions().get(0);

        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance()).isEqualTo(BigDecimal.valueOf(4500));
        assertThat(bankAccountDto.minimumBalance()).isEqualTo(BigDecimal.valueOf(1500));
        assertThat(bankAccountDto.active()).isTrue();


        assertThat(transactionDto.amount()).isEqualTo(BigDecimal.valueOf(4500));
        assertThat(transactionDto.type()).isEqualTo(TransactionType.DEPOSIT);
    }

    @Test
    void toDao() {

        BankAccountDto bankAccountDto = new BankAccountDto("theodore.roosevelt@gmail.com",
                "Theodore", "Roosevelt",
                BigDecimal.valueOf(3000), BigDecimal.valueOf(1000), false, List.of(new TransactionDto(BigDecimal.valueOf(1000), TransactionType.DEPOSIT, LocalDateTime.now(Clock.systemDefaultZone()))));

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