package com.ml.testsexamples.mappers;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dao.Transaction;
import com.ml.testsexamples.dto.BankAccountDto;
import com.ml.testsexamples.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountDto toDto(BankAccount bankAccount);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    BankAccount toDao(BankAccountDto bankAccountDto);

    default List<TransactionDto> mapTransactions(List<Transaction> transactions) {
        return Optional.ofNullable(transactions)
                .map(transactionsList -> transactionsList.stream()
                        .map(TransactionMapper.INSTANCE::toDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}