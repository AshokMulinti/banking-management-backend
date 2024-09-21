package com.bankingmanagement.service;

import com.bankingmanagement.document.Customer;
import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerTO;
import com.bankingmanagement.mongoRepository.CustomerMongoDbRepository;
import com.bankingmanagement.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMongoDbRepository customerMongoDbRepository;
    @Override
    public List<CustomerTO> findAll() throws CustomerDetailsNotFoundException {
        log.info("Inside CustomerServiceImpl.findAll");
        List<Customer> customerList = customerMongoDbRepository.findAll();
       // log.info("Number of customers fetched: {}", customerList.size());
        if (CollectionUtils.isEmpty(customerList)) {
            log.error("Customer details not found");
            throw new CustomerDetailsNotFoundException("Customer details not found");
        }
        List<CustomerTO> customerTOS = customerList.stream().map(customer -> {
            return new CustomerTO(customer.getId(), customer.getCustId(), customer.getCustName(), customer.getCustPhone(), customer.getCustAddress());
        }).collect(Collectors.toList());
        log.info("Customer details:{}", customerTOS);
        return customerTOS;
    }
    @Override
    public CustomerTO findById(String id) throws CustomerDetailsNotFoundException {
        log.info("Inside the CustomerServiceImpl.findById, id:{}", id);
        Optional<Customer> customerOptional = customerMongoDbRepository.findById(id);

        if (customerOptional.isEmpty()) {
            log.error("Customer details not found for id: {}", id);
            throw new CustomerDetailsNotFoundException("Customer details not found");
        }

        Customer customer = customerOptional.get();
        log.info("Customer details:{}", customer);

        return new CustomerTO(customer.getId(), customer.getCustId(), customer.getCustName(), customer.getCustPhone(), customer.getCustAddress());
    }
}
