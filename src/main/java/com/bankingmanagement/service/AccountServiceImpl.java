package com.bankingmanagement.service;

import com.bankingmanagement.document.Account;
import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountRequest;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.model.AccountUpdateRequest;
import com.bankingmanagement.mongoRepository.AccountMongoDbRepository;
import com.bankingmanagement.repository.AccountRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
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
    @Override
    public AccountTO findByAccountType(String accountType) throws AccountDetailsNotFoundException {
        log.info("Inside the AccountServiceImpl.findByAccountType, accountType:{}", accountType);
        Account account = accountMongoDbRepository.findByAccountType(accountType);

        if (Objects.isNull(account)) {
            log.error("Account details not found for account type:{}", accountType);
            throw new AccountDetailsNotFoundException("Account details not found");
        }

        log.info("Account details:{}", account);

        AccountTO accountTO = new AccountTO(account.getId(), account.getAccountNo(), account.getAccountType(), account.getAccountBalance());
        return accountTO;
    }
    @Override
    public void deleteById(String id) throws AccountDetailsNotFoundException {
        log.info("Inside AccountServiceImpl.deleteById, id: {}", id);

        if (!accountMongoDbRepository.existsById(id)) {
            log.error("Account details not found for id: {}", id);
            throw new AccountDetailsNotFoundException("Account details not found");
        }

        accountMongoDbRepository.deleteById(id);
        log.info("Account with id {} deleted successfully", id);
    }

    @Override
    public AccountTO save(AccountRequest accountRequest) throws AccountDetailsNotFoundException {
        log.info("Inside AccountServiceImpl.save, accountRequest: {}", accountRequest);

        Account account = new Account();
        account.setAccountNo(accountRequest.getAccountNo());
        account.setAccountType(accountRequest.getAccountType());
        account.setAccountBalance(accountRequest.getAccountBalance());

        Account accountResponse = accountMongoDbRepository.save(account);
        log.info("Account Response: {}", accountResponse);

        if (Objects.isNull(accountResponse)) {
            log.error("Account details not saved");
            throw new AccountDetailsNotFoundException("Account details not found");
        }

        return new AccountTO(accountResponse.getId(), accountResponse.getAccountNo(), accountResponse.getAccountType(), accountResponse.getAccountBalance());
    }

    @Override
    public AccountTO update(AccountUpdateRequest accountRequest) throws AccountDetailsNotFoundException {
        log.info("Inside AccountServiceImpl.update, accountRequest: {}", accountRequest);

        Optional<Account> accountOptional = accountMongoDbRepository.findById(accountRequest.getId());
        if (accountOptional.isEmpty()) {
            log.error("Account details not found");
            throw new AccountDetailsNotFoundException("Account details not found");
        }

        Account account = accountOptional.get();
        if (accountRequest.getAccountNo() > 0) {
            account.setAccountNo(accountRequest.getAccountNo());
        }
        if (accountRequest.getAccountType() != null) {
            account.setAccountType(accountRequest.getAccountType());
        }
        if (accountRequest.getAccountBalance() >= 0) {
            account.setAccountBalance(accountRequest.getAccountBalance());
        }

        Account updatedAccount = accountMongoDbRepository.save(account);
        if (Objects.isNull(updatedAccount)) {
            log.error("Account details not updated");
            throw new AccountDetailsNotFoundException("Account details not updated");
        }

        return new AccountTO(updatedAccount.getId(), updatedAccount.getAccountNo(), updatedAccount.getAccountType(), updatedAccount.getAccountBalance());
    }
}
