package com.ml.testsexamples.repositories;

import com.ml.testsexamples.dao.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}