package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerRequest;
import com.bankingmanagement.model.CustomerTO;
import com.bankingmanagement.model.CustomerUpdateRequest;

import java.util.List;

public interface CustomerService {
    List<CustomerTO> findAll() throws CustomerDetailsNotFoundException;
    CustomerTO findById(String id) throws CustomerDetailsNotFoundException;
    CustomerTO findByCustName(String custName) throws CustomerDetailsNotFoundException;
    void deleteById(String id) throws CustomerDetailsNotFoundException;
    CustomerTO save(CustomerRequest customerRequest) throws CustomerDetailsNotFoundException;
    CustomerTO update(CustomerUpdateRequest customerRequest) throws CustomerDetailsNotFoundException;
}
