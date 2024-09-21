package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankTO;

import java.util.List;

public interface BankService {
    List<BankTO> findAll() throws BankDetailsNotFoundException;
    BankTO findById(String id)throws BankDetailsNotFoundException;
}
