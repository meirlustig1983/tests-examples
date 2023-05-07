package com.ml.testsexamples.managers;

import com.ml.testsexamples.dto.CustomerDataDto;
import com.ml.testsexamples.enums.CustomerDataFields;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import com.ml.testsexamples.services.BankAccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MockitoSettings
public class BankManagerTest {

    @Mock
    private BankAccountService service;

    @InjectMocks
    private BankManager manager;

    @Test
    @DisplayName("Test deposit to customer. Customer[id = 1]")
    public void deposit() {

        CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();
        CustomerDataDto original = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3500)
                .minimumBalance(1500)
                .build();

        CustomerDataDto updated = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3550)
                .minimumBalance(1500)
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));
        when(service.update(1L, List.of(Pair.of(CustomerDataFields.BALANCE, "3550.0")))).thenReturn(Optional.of(updated));

        Optional<CustomerDataDto> result = manager.deposit(1L, 50);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(3550, result.get().getBalance());
        assertEquals(1500, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());
    }

    @Test
    @DisplayName("Test deposit to not-exists customer. Customer[id = 3], result=EntityNotFoundException")
    public void deposit_WithNotExistsCustomer() {
        when(service.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> manager.deposit(3L, 50));
    }


    @Test
    @DisplayName("Test withdraw from a customer. Customer[id = 1]")
    public void withdraw() {

        CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();
        CustomerDataDto original = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3500)
                .minimumBalance(1500)
                .build();

        CustomerDataDto updated = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(1500)
                .minimumBalance(1500)
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));
        when(service.update(1L, List.of(Pair.of(CustomerDataFields.BALANCE, "1500.0")))).thenReturn(Optional.of(updated));

        Optional<CustomerDataDto> result = manager.withdraw(1L, 2000);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(1500, result.get().getBalance());
        assertEquals(1500, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());
    }

    @Test
    @DisplayName("Test withdraw from not-exists customer. Customer[id = 3], result=EntityNotFoundException")
    public void withdraw_WithNotExistsCustomer() {
        when(service.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> manager.withdraw(3L, 50));
    }

    @Test
    @DisplayName("Test a withdraw from a customer with not enough money in his account. Customer[id = 1], result=InsufficientFundsException")
    public void withdraw_WithNInsufficientFundsException() {
        CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();
        CustomerDataDto original = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3500)
                .minimumBalance(1500)
                .build();

        when(service.findById(1L)).thenReturn(Optional.of(original));
        assertThrows(InsufficientFundsException.class, () -> manager.withdraw(1L, 2001));
    }
}