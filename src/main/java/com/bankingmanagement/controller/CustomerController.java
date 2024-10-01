package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerRequest;
import com.bankingmanagement.model.CustomerTO;
import com.bankingmanagement.model.CustomerUpdateRequest;
import com.bankingmanagement.service.CustomerService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/customername")
    public ResponseEntity<CustomerTO> findByCustName(@RequestParam("custName") String custName) {
        log.info("Inside the CustomerController.findByCustName, custName:{}", custName);
        CustomerTO customer = null;
        try {
            customer = customerService.findByCustName(custName);
            log.info("Customer details, customer:{}", customer);
        } catch (CustomerDetailsNotFoundException ex) {
            log.error("Customer details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting the customer details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of CustomerController.findByCustName");
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        log.info("Inside CustomerController.deleteById, id: {}", id);

        try {
            customerService.deleteById(id);
            log.info("Customer with id {} deleted successfully", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        } catch (CustomerDetailsNotFoundException ex) {
            log.error("Customer details not found for id {}", id, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception while deleting the customer", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<CustomerTO> update(@RequestBody @NotNull CustomerUpdateRequest customerUpdateRequest) {
        log.info("Inside CustomerController.update, customerUpdateRequest: {}", customerUpdateRequest);

        try {
            CustomerTO customerTO = customerService.update(customerUpdateRequest);
            return new ResponseEntity<>(customerTO, HttpStatus.OK);
        } catch (CustomerDetailsNotFoundException ex) {
            log.error("Customer details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception while updating customer details", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<CustomerTO> save(@RequestBody CustomerRequest customerRequest) {
        log.info("Inside CustomerController.save, customerRequest: {}", customerRequest);

        try {
            CustomerTO customerTO = customerService.save(customerRequest);
            return new ResponseEntity<>(customerTO, HttpStatus.CREATED); // 201 Created for successful creation
        } catch (CustomerDetailsNotFoundException ex) {
            log.error("Customer details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            log.error("Exception while saving customer details", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
