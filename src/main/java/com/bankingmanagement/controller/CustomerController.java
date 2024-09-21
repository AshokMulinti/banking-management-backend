package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerTO;
import com.bankingmanagement.service.CustomerService;
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
@RequestMapping("/api/v1/customers")

public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @GetMapping
    public ResponseEntity<List<CustomerTO>> findAll() {
        log.info("Inside CustomerController.findAllCustomers");

        List<CustomerTO> customerTOS;
        try {
            customerTOS = customerService.findAll();
            log.info("Customer details:{}", customerTOS);
        } catch (CustomerDetailsNotFoundException ex) {
            log.error("Customer details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting customer details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of CustomerController.findAllCustomers");
        return new ResponseEntity<>(customerTOS, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerTO> findById(@PathVariable("id") String id) {
        log.info("Inside the CustomerController.findById, id:{}", id);
        CustomerTO customer;
        try {
            customer = customerService.findById(id);
            log.info("Customer details: {}", customer);
        } catch (CustomerDetailsNotFoundException ex) {
            log.error("Customer details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting customer details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of CustomerController.findById");
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
