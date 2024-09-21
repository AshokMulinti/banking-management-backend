package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerTO;

import java.util.List;

public interface CustomerService {
    List<CustomerTO> findAll() throws CustomerDetailsNotFoundException;
    CustomerTO findById(String id) throws CustomerDetailsNotFoundException;
}
