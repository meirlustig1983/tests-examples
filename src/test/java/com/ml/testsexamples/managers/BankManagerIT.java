package com.ml.testsexamples.managers;

import com.ml.testsexamples.dto.CustomerDataDto;
import com.ml.testsexamples.exceptions.InsufficientFundsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Transactional
public class BankManagerIT {

    @Autowired
    private BankManager manager;

    @Test
    public void deposit() {

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
    public void deposit_WithNotExistsCustomer() {
        assertThrows(EntityNotFoundException.class, () -> manager.deposit(3L, 50));
    }

    @Test
    public void withdraw() {

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
    public void withdraw_FewTimes() {

        Optional<CustomerDataDto> result = manager.withdraw(1L, 1000);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(2500, result.get().getBalance());
        assertEquals(1500, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());

        result = manager.withdraw(1L, 1000);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(1500, result.get().getBalance());
        assertEquals(1500, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());

        assertThrows(InsufficientFundsException.class, () -> manager.withdraw(1L, 1000));
    }

    @Test
    public void withdraw_WithNotExistsCustomer() {
        assertThrows(EntityNotFoundException.class, () -> manager.withdraw(3L, 50));
    }

    @Test
    public void withdraw_WithNInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class, () -> manager.withdraw(1L, 2001));
    }

}