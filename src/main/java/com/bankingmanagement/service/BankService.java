package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankRequest;
import com.bankingmanagement.model.BankTO;
import com.bankingmanagement.model.BankUpdateRequest;

import java.util.List;

public interface BankService {
    List<BankTO> findAll() throws BankDetailsNotFoundException;
    BankTO findById(String id)throws BankDetailsNotFoundException;
    BankTO findByBankName(String bankName) throws BankDetailsNotFoundException;
    void deleteById(String id) throws BankDetailsNotFoundException;
    BankTO save(BankRequest bankRequest) throws BankDetailsNotFoundException;
    BankTO update(BankUpdateRequest bankRequest) throws BankDetailsNotFoundException;
}
