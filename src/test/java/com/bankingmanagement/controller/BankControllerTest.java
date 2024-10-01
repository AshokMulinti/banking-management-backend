package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankRequest;
import com.bankingmanagement.model.BankTO;
import com.bankingmanagement.model.BankUpdateRequest;
import com.bankingmanagement.service.BankService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BankControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    @Test
    public void findAll_whenBankDetailsExist_thenReturnBankData() throws Exception {
        List<BankTO> bankTOS = new ArrayList<>();
        BankTO bankTO = new BankTO("12345",2, "IDBI", "Bangalore");
        bankTOS.add(bankTO);
        when(bankService.findAll()).thenReturn(bankTOS);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/banks")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
    @Test
    public void findAll_whenBankDetailsNotExist_thenThrowException() throws Exception {
        when(bankService.findAll()).thenThrow(NullPointerException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/banks")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError());
    }
    @Test
    public void findAll_whenBankDetailsNotExist_thenThrowBankDetailsNotFoundException() throws Exception {
        when(bankService.findAll()).thenThrow(BankDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/banks")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
    @Test
    public void save_whenValidInput_thenSaveBankDetails() throws Exception {
        BankTO bankTO = new BankTO("12345",1, "Union", "Bangalore");
        when(bankService.save(any())).thenReturn(bankTO);

        BankRequest bankRequest = new BankRequest();
        bankRequest.setBankCode(1);
        bankRequest.setBankName("Union");
        bankRequest.setBankAddress("Bangalore");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString =  mapper.writeValueAsString(bankRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/banks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
    @Test
    public void save_whenInvalidBankDetails_thenThrowBankDetailsNotFoundException() throws Exception {
        BankRequest bankRequest = new BankRequest();
        bankRequest.setBankCode(1); // Valid bank code
        bankRequest.setBankName(""); // Invalid bank name
        bankRequest.setBankAddress("Bangalore");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(bankRequest);

        when(bankService.save(any())).thenThrow(BankDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/banks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
    @Test
    public void findById_whenValidId_thenReturnBankDetails() throws Exception {
        BankTO bankTO = new BankTO("12345", 2, "IDBI", "Bangalore");
        when(bankService.findById("12345")).thenReturn(bankTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/banks/12345")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findById_whenInvalidId_thenThrowBankDetailsNotFoundException() throws Exception {
        when(bankService.findById("invalidId")).thenThrow(BankDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/banks/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void deleteById_whenValidId_thenReturnOk() throws Exception {
        //when(bankService.deleteById("12345")).thenReturn(null);
        doNothing().when(bankService).deleteById("12345");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/banks/12345")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void deleteById_whenInvalidId_thenThrowBankDetailsNotFoundException() throws Exception {
      //  when(bankService.deleteById("invalidId")).thenThrow(BankDetailsNotFoundException.class);
        doThrow(BankDetailsNotFoundException.class).when(bankService).deleteById("invalidId");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/banks/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void update_whenValidRequest_thenUpdateBankDetails() throws Exception {
        BankTO bankTO = new BankTO("12345", 1, "Union", "Bangalore");
        BankUpdateRequest bankUpdateRequest = new BankUpdateRequest();
        bankUpdateRequest.setBankCode(1);
        bankUpdateRequest.setBankName("Union Updated");
        bankUpdateRequest.setBankAddress("Bangalore Updated");

        when(bankService.update(any())).thenReturn(bankTO);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(bankUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/banks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void update_whenInvalidRequest_thenThrowBankDetailsNotFoundException() throws Exception {
        BankUpdateRequest bankUpdateRequest = new BankUpdateRequest();
        bankUpdateRequest.setBankCode(1); // Valid bank code
        bankUpdateRequest.setBankName(""); // Invalid bank name
        bankUpdateRequest.setBankAddress("Bangalore");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(bankUpdateRequest);

        when(bankService.update(any())).thenThrow(BankDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/banks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findByBankName_whenValidName_thenReturnBankDetails() throws Exception {
        BankTO bankTO = new BankTO("12345", 2, "IDBI", "Bangalore");
        when(bankService.findByBankName("IDBI")).thenReturn(bankTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/banks/bankname?bankName=IDBI")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findByBankName_whenInvalidName_thenThrowBankDetailsNotFoundException() throws Exception {
        when(bankService.findByBankName("Invalid Bank")).thenThrow(BankDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/banks/bankname?bankName=Invalid Bank")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}
