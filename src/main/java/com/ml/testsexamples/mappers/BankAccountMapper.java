package com.ml.testsexamples.mappers;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dto.BankAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountDto toDto(BankAccount bankAccount);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BankAccount toDao(BankAccountDto bankAccountDto);
}