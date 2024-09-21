package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.LoanDetailsNotFoundException;
import com.bankingmanagement.model.LoanTO;
import com.bankingmanagement.service.BankService;
import com.bankingmanagement.service.LoanService;
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
@RequestMapping("/api/v1/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping()
    public ResponseEntity<List<LoanTO>> findAll() {
        log.info("Inside LoanController.findAllLoans");

        List<LoanTO> loanTOS;
        try {
            loanTOS = loanService.findAll();
            log.info("Loan details:{}", loanTOS);
        } catch (LoanDetailsNotFoundException ex) {
            log.error("Loan details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting loan details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of LoanController.findAllLoans");
        return new ResponseEntity<>(loanTOS, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanTO> findById(@PathVariable("id") String id) {
        log.info("Inside the LoanController.findById, id:{}", id);
        LoanTO loan;
        try {
            loan = loanService.findById(id);
            log.info("Loan details: {}", loan);
        } catch (LoanDetailsNotFoundException ex) {
            log.error("Loan details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting loan details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of LoanController.findById");
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }
}
