package com.ml.testsexamples.mappers;

import com.ml.testsexamples.dao.Transaction;
import com.ml.testsexamples.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDto toDto(Transaction transaction);
}