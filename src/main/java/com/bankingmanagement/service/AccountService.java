package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountRequest;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.model.AccountUpdateRequest;
import com.bankingmanagement.repository.AccountRepository;
import com.bankingmanagement.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface AccountService {
    List<AccountTO> findAll() throws AccountDetailsNotFoundException;
    AccountTO findById(String id) throws AccountDetailsNotFoundException;
    AccountTO findByAccountType(String accountType) throws AccountDetailsNotFoundException;
    void deleteById(String id) throws AccountDetailsNotFoundException;
    AccountTO save(AccountRequest accountRequest) throws AccountDetailsNotFoundException;
    AccountTO update(AccountUpdateRequest accountRequest) throws AccountDetailsNotFoundException;
}
