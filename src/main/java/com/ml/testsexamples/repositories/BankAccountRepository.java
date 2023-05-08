package com.ml.testsexamples.repositories;

import com.ml.testsexamples.dto.BankAccount;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    @NonNull List<BankAccount> findAll();
}