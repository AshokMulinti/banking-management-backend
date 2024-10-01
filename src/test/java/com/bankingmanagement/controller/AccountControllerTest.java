package com.bankingmanagement.controller;
import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountRequest;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.model.AccountUpdateRequest;
import com.bankingmanagement.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void findAll_whenAccountDetailsExist_thenReturnAccountData() throws Exception {
        List<AccountTO> accountTOS = new ArrayList<>();
        AccountTO accountTO = new AccountTO("123", 12345, "Savings", 10000);
        accountTOS.add(accountTO);
        when(accountService.findAll()).thenReturn(accountTOS);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findAll_whenAccountDetailsNotExist_thenThrowException() throws Exception {
        when(accountService.findAll()).thenThrow(NullPointerException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError());
    }

    @Test
    public void findAll_whenAccountDetailsNotExist_thenThrowAccountDetailsNotFoundException() throws Exception {
        when(accountService.findAll()).thenThrow(AccountDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findByAccountType_whenValidAccountType_thenReturnAccountDetails() throws Exception {
        AccountTO accountTO = new AccountTO("123", 12345, "Savings", 10000);
        when(accountService.findByAccountType("Savings")).thenReturn(accountTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/accounttype")
                .param("accountType", "Savings")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findByAccountType_whenInvalidAccountType_thenThrowAccountDetailsNotFoundException() throws Exception {
        when(accountService.findByAccountType("InvalidType")).thenThrow(AccountDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/accounttype")
                .param("accountType", "InvalidType")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findByAccountType_whenInvalidAccountType_thenThrowException() throws Exception {
        when(accountService.findByAccountType("InvalidType")).thenThrow(NullPointerException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/accounttype")
                .param("accountType", "InvalidType")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError());
    }
    @Test
    public void save_whenValidInput_thenSaveAccountDetails() throws Exception {
        AccountTO accountTO = new AccountTO("123", 12345, "Savings", 10000);
        when(accountService.save(any())).thenReturn(accountTO);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNo(12345);
        accountRequest.setAccountType("Savings");
        accountRequest.setAccountBalance(10000);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(accountRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void save_whenInvalidAccountDetails_thenThrowAccountDetailsNotFoundException() throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNo(12345); // Valid account number
        accountRequest.setAccountType(""); // Invalid account type
        accountRequest.setAccountBalance(10000);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(accountRequest);

        when(accountService.save(any())).thenThrow(AccountDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findById_whenValidId_thenReturnAccountDetails() throws Exception {
        AccountTO accountTO = new AccountTO("123", 12345, "Savings", 10000);
        when(accountService.findById("123")).thenReturn(accountTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/123")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findById_whenInvalidId_thenThrowAccountDetailsNotFoundException() throws Exception {
        when(accountService.findById("invalidId")).thenThrow(AccountDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/accounts/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void deleteById_whenValidId_thenReturnOk() throws Exception {
        doNothing().when(accountService).deleteById("123");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/accounts/123")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void deleteById_whenInvalidId_thenThrowAccountDetailsNotFoundException() throws Exception {
        doThrow(AccountDetailsNotFoundException.class).when(accountService).deleteById("invalidId");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/accounts/invalidId")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void update_whenValidRequest_thenUpdateAccountDetails() throws Exception {
        AccountTO accountTO = new AccountTO("123", 12345, "Savings", 10000);
        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
        accountUpdateRequest.setAccountNo(12345);
        accountUpdateRequest.setAccountType("Savings Updated");
        accountUpdateRequest.setAccountBalance(12000);

        when(accountService.update(any())).thenReturn(accountTO);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(accountUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void update_whenInvalidRequest_thenThrowAccountDetailsNotFoundException() throws Exception {
        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
        accountUpdateRequest.setAccountNo(12345); // Valid account number
        accountUpdateRequest.setAccountType(""); // Invalid account type
        accountUpdateRequest.setAccountBalance(12000);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(accountUpdateRequest);

        when(accountService.update(any())).thenThrow(AccountDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}