package com.ml.testsexamples.facades;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.enums.BankAccountFields;
import com.ml.testsexamples.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataFacade {

    private final BankAccountRepository bankAccountRepository;

    public List<BankAccount> findAllBankAccounts() {
        log.info("DataFacade.findAllBankAccounts() - retrieving all bank accounts data.");
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> findBankAccountById(Long id) {
        log.info("DataFacade.findBankAccountById(id) - retrieving bank account data by id. id: {}", id);
        return bankAccountRepository.findById(id);
    }

    public Optional<BankAccount> saveBankAccount(BankAccount bankAccount) {
        log.info("DataFacade.saveBankAccount(bankAccount) - save bank account. id: {}", bankAccount.getId());
        return Optional.of(bankAccountRepository.save(bankAccount));
    }

    public Optional<BankAccount> updateBankAccount(Long id, List<Pair<BankAccountFields, String>> data) {
        log.info("DataFacade.updateBankAccount(id, data) - update bank account data by id an list of pairs. id: {}, data: {}", id, data);
        Optional<BankAccount> original = bankAccountRepository.findById(id);
        if (original.isEmpty()) {
            return Optional.empty();
        } else {
            BankAccount.BankAccountBuilder builder = BankAccount.builder();
            BankAccount updated = builder
                    .id(original.get().getId())
                    .firstName(original.get().getFirstName())
                    .lastName(original.get().getLastName())
                    .balance(original.get().getBalance())
                    .minimumBalance(original.get().getMinimumBalance())
                    .active(original.get().isActive())
                    .createdAt(original.get().getCreatedAt())
                    .build();

            for (Pair<BankAccountFields, String> pair : data) {
                switch (pair.getFirst()) {
                    case FIRST_NAME -> updated.setFirstName(pair.getSecond());
                    case LAST_NAME -> updated.setLastName(pair.getSecond());
                    case BALANCE -> updated.setBalance(BigDecimal.valueOf(Double.parseDouble(pair.getSecond())));
                    case MINIMUM_BALANCE ->
                            updated.setMinimumBalance(BigDecimal.valueOf(Double.parseDouble(pair.getSecond())));
                    case ACTIVE -> updated.setActive(pair.getSecond().equalsIgnoreCase("true"));
                    default -> throw new IllegalArgumentException("You are unauthorized to update this field.");
                }
            }
            return Optional.of(bankAccountRepository.save(updated));
        }
    }

    public void deleteBankAccountById(Long id) {
        log.info("DataFacade.deleteBankAccountById(id) - delete bank account by id. id: {}", id);
        bankAccountRepository.deleteById(id);
    }
}