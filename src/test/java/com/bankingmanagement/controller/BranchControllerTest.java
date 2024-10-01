package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchRequest;
import com.bankingmanagement.model.BranchTO;
import com.bankingmanagement.model.BranchUpdateRequest;
import com.bankingmanagement.service.BranchService;
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
public class BranchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BranchService branchService;

    @Test
    public void findAll_whenBranchDetailsExist_thenReturnBranchData() throws Exception {
        List<BranchTO> branchTOS = new ArrayList<>();
        BranchTO branchTO = new BranchTO("1", 101, "MG Road", "Bangalore Main", 560001);
        branchTOS.add(branchTO);
        when(branchService.findAll()).thenReturn(branchTOS);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findAll_whenBranchDetailsNotExist_thenThrowBranchDetailsNotFoundException() throws Exception {
        when(branchService.findAll()).thenThrow(BranchDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void save_whenValidInput_thenSaveBranchDetails() throws Exception {
        BranchTO branchTO = new BranchTO("1", 101, "MG Road", "Bangalore Main", 560001);
        when(branchService.save(any())).thenReturn(branchTO);

        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setBranchId(101);
        branchRequest.setBranchAddress("MG Road");
        branchRequest.setBranchName("Bangalore Main");
        branchRequest.setBranchCode(560001);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(branchRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    @Test
    public void save_whenInvalidBranchDetails_thenThrowBranchDetailsNotFoundException() throws Exception {
        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setBranchId(101);
        branchRequest.setBranchAddress("MG Road");
        branchRequest.setBranchName(""); // Invalid name
        branchRequest.setBranchCode(560001);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(branchRequest);

        when(branchService.save(any())).thenThrow(BranchDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    public void findById_whenValidId_thenReturnBranchDetails() throws Exception {
        BranchTO branchTO = new BranchTO("1", 101, "MG Road", "Bangalore Main", 560001);
        when(branchService.findById("1")).thenReturn(branchTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/branches/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findById_whenInvalidId_thenThrowBranchDetailsNotFoundException() throws Exception {
        when(branchService.findById("invalidId")).thenThrow(BranchDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/branches/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void deleteById_whenValidId_thenReturnOk() throws Exception {
        doNothing().when(branchService).deleteById("1");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/branches/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Test
    public void deleteById_whenInvalidId_thenThrowBranchDetailsNotFoundException() throws Exception {
        doThrow(BranchDetailsNotFoundException.class).when(branchService).deleteById("invalidId");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/branches/invalidId")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void update_whenValidRequest_thenUpdateBranchDetails() throws Exception {
        BranchTO branchTO = new BranchTO("1", 101, "MG Road", "Bangalore Main Updated", 560001);
        BranchUpdateRequest branchUpdateRequest = new BranchUpdateRequest();
        branchUpdateRequest.setBranchId(101);
        branchUpdateRequest.setBranchAddress("MG Road");
        branchUpdateRequest.setBranchName("Bangalore Main Updated");
        branchUpdateRequest.setBranchCode(560001);

        when(branchService.update(any())).thenReturn(branchTO);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(branchUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void update_whenInvalidRequest_thenThrowBranchDetailsNotFoundException() throws Exception {
        BranchUpdateRequest branchUpdateRequest = new BranchUpdateRequest();
        branchUpdateRequest.setBranchId(101);
        branchUpdateRequest.setBranchAddress("MG Road");
        branchUpdateRequest.setBranchName(""); // Invalid name
        branchUpdateRequest.setBranchCode(560001);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(branchUpdateRequest);

        when(branchService.update(any())).thenThrow(BranchDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void findByBranchName_whenValidName_thenReturnBranchDetails() throws Exception {
        BranchTO branchTO = new BranchTO("1", 101, "MG Road", "Bangalore Main", 560001);
        when(branchService.findByBranchName("Bangalore Main")).thenReturn(branchTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/branches/branchname?branchName=Bangalore Main")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void findByBranchName_whenInvalidName_thenThrowBranchDetailsNotFoundException() throws Exception {
        when(branchService.findByBranchName("Invalid Branch")).thenThrow(BranchDetailsNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/branches/branchname?branchName=Invalid Branch")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}
