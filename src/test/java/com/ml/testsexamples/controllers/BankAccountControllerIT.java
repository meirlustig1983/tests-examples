package com.ml.testsexamples.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ml.testsexamples.dto.BankAccountDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankAccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void createFirstAccount() throws Exception {
        BankAccountDto accountDto = new BankAccountDto("john.doe@gmail.com", "John", "Doe", BigDecimal.valueOf(4500), BigDecimal.valueOf(1500), false);
        mockMvc.perform(post("/api/v1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(4500))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(false))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(1)
    void createSecondAccount() throws Exception {
        BankAccountDto accountDto = new BankAccountDto("meir.lustig@gmail.com", "Meir", "Lustig", BigDecimal.valueOf(45000), BigDecimal.valueOf(-1500), false);
        mockMvc.perform(post("/api/v1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45000))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(false))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(1)
    void createAccountWithWrongFormatAccountId() throws Exception {
        BankAccountDto accountDto = new BankAccountDto("johndoe", "John", "Doe", BigDecimal.valueOf(4500), BigDecimal.valueOf(1500), false);
        mockMvc.perform(post("/api/v1/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts"))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(2)
    void getAccountInfoForFirstAccount() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(4500))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(false))
                .andDo(document("{method-name}"));
    }


    @Test
    @Order(2)
    void getAccountInfoForSecondAccount() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45000))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(false))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(2)
    void getAccountInfoForWrongFormatAccountId() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "john.doegmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/john.doegmail.com"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(2)
    void getAccountInfoForNoExistsAccount() throws Exception {
        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/no.exists@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void makeDepositToInactiveAccount() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .param("amount", "500")
                        .param("accountId", "john.doe@gmail.com"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Inactive bank account"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void makeWithdrawFromInactiveAccount() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .param("amount", "500")
                        .param("accountId", "meir.lustig@gmail.com"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Inactive bank account"))
                .andExpect(jsonPath("$.statusCode").value(500))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void makeDepositWithWrongFormatAccountId() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .param("amount", "500")
                        .param("accountId", "john.doegmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void makeWithdrawWithWrongFormatAccountId() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .param("amount", "500")
                        .param("accountId", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void makeDepositWithNoExistsAccountId() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .param("amount", "500")
                        .param("accountId", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(3)
    void makeWithdrawWithNoExistsAccountId() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .param("amount", "500")
                        .param("accountId", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(4)
    void activateFirstAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/activate/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(4500))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(4)
    void activateSecondAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/activate/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45000))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(4)
    void activateWithWrongFormatAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/activate/{accountId}", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/activate/meir.lustiggmail.com"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(4)
    void activateWithNoExistsAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/activate/{accountId}", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/activate/no.exists@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(5)
    void makeDepositToFirstAccount() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .param("amount", "500")
                        .param("accountId", "john.doe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(5000))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(5)
    void makeDepositToSecondAccount() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .param("amount", "500")
                        .param("accountId", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(45500))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(6)
    void makeWithdrawFromSecondAccount() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .param("amount", "47000")
                        .param("accountId", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(-1500))
                .andExpect(jsonPath("$.minimumBalance").value(-1500))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(7)
    void makeWithdrawFromSecondAccountOverTheMinimum() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .param("amount", "1")
                        .param("accountId", "meir.lustig@gmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Insufficient funds exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(8)
    void makeWithdrawFromSecondAccountWithNegativeAmount() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/withdraw")
                        .param("amount", "-1")
                        .param("accountId", "meir.lustig@gmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/withdraw"))
                .andExpect(jsonPath("$.message").value("Amount number should be positive"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(8)
    void makeDepositFromSecondAccountWithNegativeAmount() throws Exception {
        mockMvc.perform(post("/api/v1/bank-accounts/deposit")
                        .param("amount", "-1")
                        .param("accountId", "meir.lustig@gmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deposit"))
                .andExpect(jsonPath("$.message").value("Amount number should be positive"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(9)
    void deactivateAccountFirstAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/deactivate/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.balance").value(5000.00))
                .andExpect(jsonPath("$.minimumBalance").value(1500))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    @Order(9)
    void deactivateAccountSecondAccount() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/deactivate/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value("meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Meir"))
                .andExpect(jsonPath("$.lastName").value("Lustig"))
                .andExpect(jsonPath("$.balance").value(-1500.0))
                .andExpect(jsonPath("$.minimumBalance").value(-1500.0))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    @Order(9)
    void deactivateAccountWithNoExistsAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/deactivate/{accountId}", "no.exists@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deactivate/no.exists@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(9)
    void deactivateAccountWithWrongFormatAccountId() throws Exception {
        mockMvc.perform(put("/api/v1/bank-accounts/deactivate/{accountId}", "meir.lustiggmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/deactivate/meir.lustiggmail.com"))
                .andExpect(jsonPath("$.message").value("Wrong format exception"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(10)
    void deleteFirstBankAccount() throws Exception {
        mockMvc.perform(delete("/api/v1/bank-accounts/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "john.doe@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/john.doe@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }

    @Test
    @Order(10)
    void deleteSecondBankAccount() throws Exception {
        mockMvc.perform(delete("/api/v1/bank-accounts/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/bank-accounts/{accountId}", "meir.lustig@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.path").value("/api/v1/bank-accounts/meir.lustig@gmail.com"))
                .andExpect(jsonPath("$.message").value("Invalid bank account"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andDo(document("{method-name}"));
    }
}
