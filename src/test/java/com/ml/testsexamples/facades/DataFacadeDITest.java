package com.ml.testsexamples.facades;

import com.ml.testsexamples.dao.BankAccount;
import com.ml.testsexamples.dao.BankAccountParameterResolver;
import com.ml.testsexamples.repositories.BankAccountRepository;
import com.ml.testsexamples.utils.CustomDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@MockitoSettings
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
@ExtendWith(BankAccountParameterResolver.class)
public class DataFacadeDITest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private DataFacade dataFacade;

    @Test
    public void findAllBankAccounts(BankAccount bankAccount) {

        when(repository.findAll()).thenReturn(List.of(bankAccount));

        List<BankAccount> result = dataFacade.findAllBankAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("theodore.roosevelt@gmail.com", result.get(0).getAccountId());
        assertEquals("Theodore", result.get(0).getFirstName());
        assertEquals("Roosevelt", result.get(0).getLastName());
        assertEquals(3500, result.get(0).getBalance().intValue());
        assertEquals(1500, result.get(0).getMinimumBalance().intValue());
        assertInstanceOf(LocalDateTime.class, result.get(0).getCreatedAt());
        assertInstanceOf(LocalDateTime.class, result.get(0).getUpdatedAt());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }
}
