package com.ml.testsexamples.controllers;

import jakarta.validation.Valid;
import com.ml.testsexamples.dto.BankAccountDto;
import com.ml.testsexamples.services.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccountDto> getAccountInfo(@PathVariable("accountId") String accountId) {
        Optional<BankAccountDto> accountInfo = bankAccountService.getAccountInfo(accountId);
        return accountInfo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BankAccountDto> createAccount(@Valid @RequestBody BankAccountDto bankAccountDto) {
        Optional<BankAccountDto> createdAccount = bankAccountService.createAccount(bankAccountDto);
        return createdAccount.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable("accountId") String accountId) {
        bankAccountService.deleteBankAccountByAccountId(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activate/{accountId}")
    public ResponseEntity<BankAccountDto> activateAccount(@PathVariable("accountId") String accountId) {
        Optional<BankAccountDto> activatedAccount = bankAccountService.activateAccount(accountId);
        return activatedAccount.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/deactivate/{accountId}")
    public ResponseEntity<BankAccountDto> deactivateAccount(@PathVariable("accountId") String accountId) {
        Optional<BankAccountDto> deactivatedAccount = bankAccountService.deactivateAccount(accountId);
        return deactivatedAccount.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/deposit")
    public ResponseEntity<BankAccountDto> makeDeposit(@RequestParam("accountId") String accountId, @RequestParam("amount") double amount) {
        Optional<BankAccountDto> updatedAccount = bankAccountService.makeDeposit(accountId, amount);
        return updatedAccount.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BankAccountDto> makeWithdraw(@RequestParam("accountId") String accountId, @RequestParam("amount") double amount) {
        Optional<BankAccountDto> updatedAccount = bankAccountService.makeWithdraw(accountId, amount);
        return updatedAccount.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}