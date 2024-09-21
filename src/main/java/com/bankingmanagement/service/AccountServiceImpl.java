package com.bankingmanagement.service;

import com.bankingmanagement.document.Account;
import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.mongoRepository.AccountMongoDbRepository;
import com.bankingmanagement.repository.AccountRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMongoDbRepository accountMongoDbRepository;
     @Override
     public List<AccountTO> findAll() throws AccountDetailsNotFoundException {
         log.info("Inside the AccountServiceImpl.findAll");
         List<Account> accountList = accountMongoDbRepository.findAll();
         if (CollectionUtils.isEmpty(accountList)) {
             log.error("Account details not found");
             throw new AccountDetailsNotFoundException("Account details not found");
         }
         List<AccountTO> accountTOS = accountList.stream().map(account -> {
             return new AccountTO(account.getId(), account.getAccountNo(), account.getAccountType(), account.getAccountBalance());
         }).collect(Collectors.toList());
         log.info("Account details: {}", accountTOS);
         return accountTOS;
     }
    @Override
    public AccountTO findById(String id) throws AccountDetailsNotFoundException {
        log.info("Inside the AccountServiceImpl.findById, id:{}", id);
        Optional<Account> accountOptional = accountMongoDbRepository.findById(id);

        if (accountOptional.isEmpty()) {
            log.error("Account details not found for id: {}", id);
            throw new AccountDetailsNotFoundException("Account details not found");
        }

        Account account = accountOptional.get();
        log.info("Account details:{}", account);

        return new AccountTO(account.getId(), account.getAccountNo(), account.getAccountType(), account.getAccountBalance());
    }
}
