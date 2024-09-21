package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.repository.AccountRepository;
import com.bankingmanagement.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface AccountService {
    List<AccountTO> findAll() throws AccountDetailsNotFoundException;
    AccountTO findById(String id) throws AccountDetailsNotFoundException;
}
