package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.LoanDetailsNotFoundException;
import com.bankingmanagement.model.LoanTO;

import java.util.List;

public interface LoanService {
    List<LoanTO> findAll() throws LoanDetailsNotFoundException;
    LoanTO findById(String id) throws LoanDetailsNotFoundException;
}
