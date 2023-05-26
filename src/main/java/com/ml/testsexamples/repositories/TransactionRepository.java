package com.ml.testsexamples.repositories;

import com.ml.testsexamples.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}