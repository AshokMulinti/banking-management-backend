package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.AccountDetailsNotFoundException;
import com.bankingmanagement.model.AccountTO;
import com.bankingmanagement.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
   //
    }
}
