package com.ml.testsexamples.services;

import com.ml.testsexamples.dto.CustomerDataDto;
import com.ml.testsexamples.enums.CustomerDataFields;
import com.ml.testsexamples.repositories.CustomerDataRepository;
import com.ml.testsexamples.utils.CustomDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MockitoSettings
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
public class BankAccountServiceTest {

    @Mock
    private CustomerDataRepository repository;

    @InjectMocks
    private BankAccountService service;

    @Test
    public void getAll() {

        CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();

        CustomerDataDto customerData1 = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3500)
                .minimumBalance(1500)
                .build();

        CustomerDataDto customerData2 = customerDataBuilder
                .id(2L)
                .firstName("Franklin")
                .lastName("Benjamin")
                .balance(0)
                .minimumBalance(-1000)
                .build();

        when(repository.findAll()).thenReturn(List.of(customerData1, customerData2));

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

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findById() {

        CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();
        CustomerDataDto customerData1 = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3500)
                .minimumBalance(1500)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(customerData1));

        Optional<CustomerDataDto> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Theodore", result.get().getFirstName());
        assertEquals("Roosevelt", result.get().getLastName());
        assertEquals(3500, result.get().getBalance());
        assertEquals(1500, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findById_FindCustomerDataForNotExistsCustomerId_EmptyOptional() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<CustomerDataDto> result = service.findById(1L);

        assertFalse(result.isPresent());

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void update() {

        CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();

        CustomerDataDto originalDto = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3500)
                .minimumBalance(1500)
                .build();

        CustomerDataDto updatedDto = customerDataBuilder
                .id(1L)
                .firstName("Meir")
                .lastName("Roth")
                .balance(10000)
                .minimumBalance(0)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(originalDto));
        when(repository.save(any(CustomerDataDto.class))).thenReturn(updatedDto);

        Optional<CustomerDataDto> result = service.update(1L, List.of(Pair.of(CustomerDataFields.FIRST_NAME, "Meir"),
                Pair.of(CustomerDataFields.LAST_NAME, "Roth"), Pair.of(CustomerDataFields.BALANCE, "10000"),
                Pair.of(CustomerDataFields.MINIMUM_BALANCE, "0")));

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Meir", result.get().getFirstName());
        assertEquals("Roth", result.get().getLastName());
        assertEquals(10000, result.get().getBalance());
        assertEquals(0, result.get().getMinimumBalance());
        assertInstanceOf(Date.class, result.get().getCreatedDate());

        verify(repository).findById(1L);
        verify(repository).save(any(CustomerDataDto.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void update_TryToUpdateBalanceFieldForNotExistsCustomer_EmptyOptional() {
        when(repository.findById(3L)).thenReturn(Optional.empty());

        Optional<CustomerDataDto> result = service.update(3L, List.of(Pair.of(CustomerDataFields.BALANCE, "1000"), Pair.of(CustomerDataFields.BALANCE, "15000")));
        assertFalse(result.isPresent());
    }

    @Test
    public void update_TryToUpdateUnauthorizedField_IllegalArgumentException() {

        CustomerDataDto.CustomerDataDtoBuilder customerDataBuilder = CustomerDataDto.builder();

        CustomerDataDto originalDto = customerDataBuilder
                .id(1L)
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(3500)
                .minimumBalance(1500)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(originalDto));

        assertThrows(IllegalArgumentException.class, () -> service.update(1L, List.of(Pair.of(CustomerDataFields.ID, "1000"), Pair.of(CustomerDataFields.BALANCE, "8500"))));

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }
}