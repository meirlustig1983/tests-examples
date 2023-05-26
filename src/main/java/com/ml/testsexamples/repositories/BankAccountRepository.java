package com.ml.testsexamples.repositories;

import com.ml.testsexamples.dao.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findBankAccountByAccountId(String accountId);

    void deleteByAccountId(String accountId);
}