package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.LoanDetailsNotFoundException;
import com.bankingmanagement.model.LoanRequest;
import com.bankingmanagement.model.LoanTO;
import com.bankingmanagement.model.LoanUpdateRequest;
import com.bankingmanagement.service.LoanService;
import jakarta.validation.constraints.NotNull;
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
public class LoanControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Test
    public void findAll_whenLoanDetailsExist_thenReturnLoanData() throws Exception {
        List<LoanTO> loanTOS = new ArrayList<>();
        LoanTO loanTO = new LoanTO("1", 1001, "Personal", 50000, 1, 1);
        loanTOS.add(loanTO);
        when(loanService.findAll()).thenReturn(loanTOS);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findAll_whenLoanDetailsNotExist_thenThrowException() throws Exception {
        when(loanService.findAll()).thenThrow(NullPointerException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError());
    }

    @Test
    public void findAll_whenLoanDetailsNotExist_thenThrowLoanDetailsNotFoundException() throws Exception {
        when(loanService.findAll()).thenThrow(LoanDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void save_whenValidInput_thenSaveLoanDetails() throws Exception {
        LoanTO loanTO = new LoanTO("1", 1001, "Personal", 50000, 1, 1);
        when(loanService.save(any())).thenReturn(loanTO);

        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setLoanId(1001);
        loanRequest.setLoanType("Personal");
        loanRequest.setLoanAmount(50000);
        loanRequest.setBranchId(1);
        loanRequest.setCustId(1);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(loanRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void save_whenInvalidLoanDetails_thenThrowLoanDetailsNotFoundException() throws Exception {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setLoanId(1001); // Valid loan ID
        loanRequest.setLoanType(""); // Invalid loan type
        loanRequest.setLoanAmount(50000);
        loanRequest.setBranchId(1);
        loanRequest.setCustId(1);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(loanRequest);

        when(loanService.save(any())).thenThrow(LoanDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findById_whenValidId_thenReturnLoanDetails() throws Exception {
        LoanTO loanTO = new LoanTO("1", 1001, "Personal", 50000, 1, 1);
        when(loanService.findById("1")).thenReturn(loanTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/loans/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findById_whenInvalidId_thenThrowLoanDetailsNotFoundException() throws Exception {
        when(loanService.findById("invalidId")).thenThrow(LoanDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/loans/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void deleteById_whenValidId_thenReturnNoContent() throws Exception {
        doNothing().when(loanService).deleteById("1");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/loans/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Test
    public void deleteById_whenInvalidId_thenThrowLoanDetailsNotFoundException() throws Exception {
        doThrow(LoanDetailsNotFoundException.class).when(loanService).deleteById("invalidId");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/loans/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void update_whenValidRequest_thenUpdateLoanDetails() throws Exception {
        LoanTO loanTO = new LoanTO("1", 1001, "Personal", 50000, 1, 1);
        LoanUpdateRequest loanUpdateRequest = new LoanUpdateRequest();
        loanUpdateRequest.setLoanId(1001);
        loanUpdateRequest.setLoanType("Personal Updated");
        loanUpdateRequest.setLoanAmount(60000);
        loanUpdateRequest.setBranchId(1);
        loanUpdateRequest.setCustId(1);

        when(loanService.update(any())).thenReturn(loanTO);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(loanUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void update_whenInvalidRequest_thenThrowLoanDetailsNotFoundException() throws Exception {
        LoanUpdateRequest loanUpdateRequest = new LoanUpdateRequest();
        loanUpdateRequest.setLoanId(1001); // Valid loan ID
        loanUpdateRequest.setLoanType(""); // Invalid loan type
        loanUpdateRequest.setLoanAmount(60000);
        loanUpdateRequest.setBranchId(1);
        loanUpdateRequest.setCustId(1);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(loanUpdateRequest);

        when(loanService.update(any())).thenThrow(LoanDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findByLoanType_whenValidType_thenReturnLoanDetails() throws Exception {
        LoanTO loanTO = new LoanTO("1", 1001, "Personal", 50000, 1, 1);
        when(loanService.findByLoanType("Personal")).thenReturn(loanTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/loans/loantype?loanType=Personal")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findByLoanType_whenInvalidType_thenThrowLoanDetailsNotFoundException() throws Exception {
        when(loanService.findByLoanType("Invalid Loan Type")).thenThrow(LoanDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/loans/loantype?loanType=Invalid Loan Type")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}
