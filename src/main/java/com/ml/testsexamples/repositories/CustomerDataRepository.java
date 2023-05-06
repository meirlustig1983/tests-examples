package com.ml.testsexamples.repositories;

import com.ml.testsexamples.dto.CustomerDataDto;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerDataRepository extends CrudRepository<CustomerDataDto, Long> {
    @NonNull List<CustomerDataDto> findAll();
}