package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerRequest;
import com.bankingmanagement.model.CustomerTO;
import com.bankingmanagement.model.CustomerUpdateRequest;
import com.bankingmanagement.service.CustomerService;
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
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void findAll_whenCustomerDetailsExist_thenReturnCustomerData() throws Exception {
        List<CustomerTO> customerTOS = new ArrayList<>();
        CustomerTO customerTO = new CustomerTO("12345", 1, "John Doe", 987654321, "123 Main St");
        customerTOS.add(customerTO);
        when(customerService.findAll()).thenReturn(customerTOS);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findAll_whenCustomerDetailsNotExist_thenThrowException() throws Exception {
        when(customerService.findAll()).thenThrow(NullPointerException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError());
    }

    @Test
    public void findAll_whenCustomerDetailsNotExist_thenThrowCustomerDetailsNotFoundException() throws Exception {
        when(customerService.findAll()).thenThrow(CustomerDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void save_whenValidInput_thenSaveCustomerDetails() throws Exception {
        CustomerTO customerTO = new CustomerTO("12345", 1, "John Doe", 987654321, "123 Main St");
        when(customerService.save(any())).thenReturn(customerTO);

        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustId(1);
        customerRequest.setCustName("John Doe");
        customerRequest.setCustPhone(987654321);
        customerRequest.setCustAddress("123 Main St");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customerRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        // Expecting 201 Created status
        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void save_whenInvalidCustomerDetails_thenThrowCustomerDetailsNotFoundException() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustId(1); // Valid customer ID
        customerRequest.setCustName(""); // Invalid customer name
        customerRequest.setCustPhone(987654321);
        customerRequest.setCustAddress("123 Main St");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customerRequest);

        when(customerService.save(any())).thenThrow(CustomerDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findById_whenValidId_thenReturnCustomerDetails() throws Exception {
        CustomerTO customerTO = new CustomerTO("12345", 1, "John Doe", 987654321, "123 Main St");
        when(customerService.findById("12345")).thenReturn(customerTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/customers/12345")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findById_whenInvalidId_thenThrowCustomerDetailsNotFoundException() throws Exception {
        when(customerService.findById("invalidId")).thenThrow(CustomerDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/customers/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void deleteById_whenValidId_thenReturnNoContent() throws Exception {
        doNothing().when(customerService).deleteById("12345");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/customers/12345")
                .contentType(MediaType.APPLICATION_JSON);
        // Expecting 204 No Content status
        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Test
    public void deleteById_whenInvalidId_thenThrowCustomerDetailsNotFoundException() throws Exception {
        doThrow(CustomerDetailsNotFoundException.class).when(customerService).deleteById("invalidId");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/customers/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void update_whenValidRequest_thenUpdateCustomerDetails() throws Exception {
        CustomerTO customerTO = new CustomerTO("12345", 1, "John Doe", 987654321, "123 Main St");
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
        customerUpdateRequest.setCustId(1);
        customerUpdateRequest.setCustName("John Doe Updated");
        customerUpdateRequest.setCustPhone(987654321);
        customerUpdateRequest.setCustAddress("123 Main St Updated");

        when(customerService.update(any())).thenReturn(customerTO);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customerUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void update_whenInvalidRequest_thenThrowCustomerDetailsNotFoundException() throws Exception {
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();
        customerUpdateRequest.setCustId(1); // Valid customer ID
        customerUpdateRequest.setCustName(""); // Invalid customer name
        customerUpdateRequest.setCustPhone(987654321);
        customerUpdateRequest.setCustAddress("123 Main St");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customerUpdateRequest);

        when(customerService.update(any())).thenThrow(CustomerDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findByCustomerName_whenValidName_thenReturnCustomerDetails() throws Exception {
        CustomerTO customerTO = new CustomerTO("12345", 1, "John Doe", 987654321, "123 Main St");
        when(customerService.findByCustName("John Doe")).thenReturn(customerTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/customers/customername?custName=John Doe")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findByCustomerName_whenInvalidName_thenThrowCustomerDetailsNotFoundException() throws Exception {
        when(customerService.findByCustName("Invalid Customer")).thenThrow(CustomerDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/customers/customername?custName=Invalid Customer")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}
