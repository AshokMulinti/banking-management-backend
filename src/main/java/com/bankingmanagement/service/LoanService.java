package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.LoanDetailsNotFoundException;
import com.bankingmanagement.model.LoanRequest;
import com.bankingmanagement.model.LoanTO;
import com.bankingmanagement.model.LoanUpdateRequest;

import java.util.List;

public interface LoanService {
    List<LoanTO> findAll() throws LoanDetailsNotFoundException;
    LoanTO findById(String id) throws LoanDetailsNotFoundException;
    LoanTO findByLoanType(String loanType) throws LoanDetailsNotFoundException;
    void deleteById(String id) throws LoanDetailsNotFoundException;
    LoanTO save(LoanRequest loanRequest) throws LoanDetailsNotFoundException;
    LoanTO update(LoanUpdateRequest loanRequest) throws LoanDetailsNotFoundException;
}
