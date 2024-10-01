package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountRequest;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.model.AccountUpdateRequest;
import com.bankingmanagement.service.AccountService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping()
    public ResponseEntity<List<AccountTO>> findAll() {
        log.info("Inside the AccountController.findAllAccounts");

        List<AccountTO> accountTOS;
        try {
            accountTOS = accountService.findAll();
            log.info("Account details: {}", accountTOS);
        } catch (AccountDetailsNotFoundException ex) {
            log.error("Account details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting account details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of AccountController.findAllAccounts");
        return new ResponseEntity<>(accountTOS, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountTO> findById(@PathVariable("id") String id) {
        log.info("Inside the AccountController.findById, id:{}", id);
        AccountTO accountTO = null;
        try {
            accountTO = accountService.findById(id);
            log.info("Account details:{}", accountTO);
        } catch (AccountDetailsNotFoundException ex) {
            log.error("Account details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting the account details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of AccountController.findById");
        return new ResponseEntity<>(accountTO, HttpStatus.OK);
    }

    @GetMapping("/accounttype")
    public ResponseEntity<AccountTO> findByAccountType(@RequestParam("accountType") String accountType) {
        log.info("Inside the AccountController.findByAccountType, accountType:{}", accountType);
        AccountTO account = null;
        try {
            account = accountService.findByAccountType(accountType);
            log.info("Account details, account:{}", account);
        } catch (AccountDetailsNotFoundException ex) {
            log.error("Account details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting the account details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of AccountController.findByAccountType");
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        log.info("Inside AccountController.deleteById, id: {}", id);
        try {
            accountService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AccountDetailsNotFoundException ex) {
            log.error("Account details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception while deleting the account", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<AccountTO> update(@RequestBody @NotNull AccountUpdateRequest accountRequest) {
        log.info("Inside AccountController.update, accountRequest: {}", accountRequest);
        AccountTO accountTO;
        try {
            accountTO = accountService.update(accountRequest);
        } catch (AccountDetailsNotFoundException ex) {
            log.error("Account details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception while updating account details", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(accountTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountTO> save(@RequestBody AccountRequest accountRequest) {
        log.info("Inside AccountController.save, accountRequest: {}", accountRequest);
        AccountTO accountTO;
        try {
            accountTO = accountService.save(accountRequest);
        } catch (AccountDetailsNotFoundException ex) {
            log.error("Account details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception while saving account details", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(accountTO, HttpStatus.OK);
    }
}
