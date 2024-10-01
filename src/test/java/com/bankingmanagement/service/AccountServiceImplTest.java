package com.bankingmanagement.service;
import com.bankingmanagement.document.Account;
import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountRequest;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.model.AccountUpdateRequest;
import com.bankingmanagement.mongoRepository.AccountMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountMongoDbRepository accountMongoDbRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    // Test for findAll
    @Test
    public void findAll_whenAccountDetailsExist_thenReturnAccountData() throws AccountDetailsNotFoundException {
        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        account.setAccountNo(12345);
        account.setAccountType("Savings");
        account.setAccountBalance(5000);
        accountList.add(account);

        when(accountMongoDbRepository.findAll()).thenReturn(accountList);

        List<AccountTO> accountTOS = accountService.findAll();
        assertEquals(1, accountTOS.size());
        assertEquals(12345, accountTOS.get(0).accountNo());
    }

    @Test
    public void findAll_whenAccountDetailsNotExist_thenThrowException() {
        when(accountMongoDbRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(AccountDetailsNotFoundException.class, () -> accountService.findAll());
    }

    // Test for findById
    @Test
    public void findById_whenAccountDetailsExist_thenReturnAccountData() throws AccountDetailsNotFoundException {
        Account account = new Account();
        account.setId("123");
        account.setAccountNo(12345);
        account.setAccountType("Savings");
        account.setAccountBalance(5000);

        when(accountMongoDbRepository.findById("123")).thenReturn(Optional.of(account));

        AccountTO accountTO = accountService.findById("123");
        assertEquals("123", accountTO.id());
        assertEquals(12345, accountTO.accountNo());
    }

    @Test
    public void findById_whenAccountDetailsNotExist_thenThrowException() {
        when(accountMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(AccountDetailsNotFoundException.class, () -> accountService.findById("123"));
    }

    // Test for findByAccountType
    @Test
    public void findByAccountType_whenAccountDetailsExist_thenReturnAccountData() throws AccountDetailsNotFoundException {
        Account account = new Account();
        account.setId("123");
        account.setAccountNo(12345);
        account.setAccountType("Savings");
        account.setAccountBalance(5000);

        when(accountMongoDbRepository.findByAccountType("Savings")).thenReturn(account);

        AccountTO accountTO = accountService.findByAccountType("Savings");
        assertEquals("123", accountTO.id());
        assertEquals("Savings", accountTO.accountType());
    }

    @Test
    public void findByAccountType_whenAccountDetailsNotExist_thenThrowException() {
        when(accountMongoDbRepository.findByAccountType("Savings")).thenReturn(null);

        assertThrows(AccountDetailsNotFoundException.class, () -> accountService.findByAccountType("Savings"));
    }

    // Test for deleteById
    @Test
    public void deleteById_whenAccountExists_thenDeleteSuccessfully() throws AccountDetailsNotFoundException {
        when(accountMongoDbRepository.existsById("123")).thenReturn(true);

        accountService.deleteById("123");

        verify(accountMongoDbRepository).deleteById("123");
    }

    @Test
    public void deleteById_whenAccountNotExist_thenThrowException() {
        when(accountMongoDbRepository.existsById("123")).thenReturn(false);

        assertThrows(AccountDetailsNotFoundException.class, () -> accountService.deleteById("123"));
    }

    // Test for save
    @Test
    public void save_whenAccountDetailsAreValid_thenReturnSavedAccount() throws AccountDetailsNotFoundException {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNo(12345);
        accountRequest.setAccountType("Savings");
        accountRequest.setAccountBalance(5000);

        Account account = new Account();
        account.setId("123");
        account.setAccountNo(12345);
        account.setAccountType("Savings");
        account.setAccountBalance(5000);

        when(accountMongoDbRepository.save(any(Account.class))).thenReturn(account);

        AccountTO accountTO = accountService.save(accountRequest);

        assertEquals("123", accountTO.id());
        assertEquals(12345, accountTO.accountNo());
        assertEquals("Savings", accountTO.accountType());
    }

    @Test
    public void save_whenAccountNotSaved_thenThrowException() {
        AccountRequest accountRequest = new AccountRequest();

        when(accountMongoDbRepository.save(any(Account.class))).thenReturn(null);

        assertThrows(AccountDetailsNotFoundException.class, () -> accountService.save(accountRequest));
    }

    // Test for update
    @Test
    public void update_whenValidUpdateRequest_thenAccountIsUpdated() throws AccountDetailsNotFoundException {
        Account account = new Account();
        account.setId("123");
        account.setAccountNo(12345);
        account.setAccountType("Savings");
        account.setAccountBalance(5000);

        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setAccountNo(67890);
        updateRequest.setAccountType("Current");
        updateRequest.setAccountBalance(10000);

        when(accountMongoDbRepository.findById("123")).thenReturn(Optional.of(account));
        when(accountMongoDbRepository.save(account)).thenReturn(account);

        AccountTO accountTO = accountService.update(updateRequest);

        assertEquals("123", accountTO.id());
        assertEquals(67890, accountTO.accountNo());
        assertEquals("Current", accountTO.accountType());
        assertEquals(10000, accountTO.accountBalance());
    }

    @Test
    public void update_whenAccountDoesNotExist_thenThrowException() {
        AccountUpdateRequest updateRequest = new AccountUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setAccountNo(67890);
        updateRequest.setAccountType("Current");
        updateRequest.setAccountBalance(10000);

        when(accountMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(AccountDetailsNotFoundException.class, () -> accountService.update(updateRequest));
    }
}
