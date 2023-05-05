package com.ml.testsexamples.services;

import com.ml.testsexamples.dto.CustomerDataDto;
import com.ml.testsexamples.enums.CustomerDataFields;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Transactional
public class BankAccountServiceIT {

    @Autowired
    private BankAccountService service;

    @Test
    public void getAll() {

        List<CustomerDataDto> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Theodore", result.get(0).getFirstName());
        assertEquals("Roosevelt", result.get(0).getLastName());
        assertEquals(3500, result.get(0).getBalance());
        assertEquals(1500, result.get(0).getMinimumBalance());
        assertInstanceOf(Date.class, result.get(0).getCreatedDate());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Franklin", result.get(1).getFirstName());
        assertEquals("Benjamin", result.get(1).getLastName());
        assertEquals(0, result.get(1).getBalance());
        assertEquals(-1000, result.get(1).getMinimumBalance());
        assertInstanceOf(Date.class, result.get(1).getCreatedDate());
    }

    @Test
    public void findById_WithFirstCustomerData() {

        Optional<CustomerDataDto> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(3500, result.get().getBalance());
        assertEquals(1500, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());
    }

    @Test
    public void findById_WithSecondCustomerData() {

        Optional<CustomerDataDto> result = service.findById(2L);

        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Franklin", result.get().getFirstName());
        assertEquals("Benjamin", result.get().getLastName());
        assertEquals(0, result.get().getBalance());
        assertEquals(-1000, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());
    }

    @Test
    public void findById_WithNotExistsCustomer() {
        Optional<CustomerDataDto> result = service.findById(3L);
        assertFalse(result.isPresent());
    }

    @Test
    public void update() {
        Optional<CustomerDataDto> result = service.update(2L, List.of(Pair.of(CustomerDataFields.FIRST_NAME, "Meir"),
                Pair.of(CustomerDataFields.LAST_NAME, "Roth"), Pair.of(CustomerDataFields.BALANCE, "10000"),
                Pair.of(CustomerDataFields.MINIMUM_BALANCE, "0")));

        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getId());
        assertEquals("Meir", result.get().getFirstName());
        assertEquals("Roth", result.get().getLastName());
        assertEquals(10000, result.get().getBalance());
        assertEquals(0, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());
    }

    @Test
    public void update_UpdateBalance_WithUnauthorizedField() {
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, List.of(Pair.of(CustomerDataFields.ID, "1000"), Pair.of(CustomerDataFields.BALANCE, "8500"))));
    }

    @Test
    public void update_UpdateBalance_WithNotExistsCustomer() {
        Optional<CustomerDataDto> result = service.update(3L, List.of(Pair.of(CustomerDataFields.BALANCE, "1000"), Pair.of(CustomerDataFields.BALANCE, "15000")));
        assertFalse(result.isPresent());
    }

    @Test
    public void findById_CustomerNotExists() {
        Optional<CustomerDataDto> result = service.findById(3L);
        assertFalse(result.isPresent());
    }
}