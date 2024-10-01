package com.bankingmanagement.service;

import com.bankingmanagement.document.Customer;
import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerRequest;
import com.bankingmanagement.model.CustomerTO;
import com.bankingmanagement.model.CustomerUpdateRequest;
import com.bankingmanagement.mongoRepository.CustomerMongoDbRepository;
import com.bankingmanagement.repository.CustomerRepository;
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

    @Override
    public CustomerTO findByCustName(String custName) throws CustomerDetailsNotFoundException {
        log.info("Inside the CustomerServiceImpl.findByCustName, custName:{}", custName);
        Customer customer = customerMongoDbRepository.findByCustName(custName);

        if (Objects.isNull(customer)) {
            log.error("Customer details not found for customer name:{}", custName);
            throw new CustomerDetailsNotFoundException("Customer details not found");
        }

        log.info("Customer details:{}", customer);

        CustomerTO customerTO = new CustomerTO(customer.getId(), customer.getCustId(), customer.getCustName(), customer.getCustPhone(), customer.getCustAddress());
        return customerTO;
    }
    @Override
    public void deleteById(String id) throws CustomerDetailsNotFoundException {
        log.info("Inside CustomerServiceImpl.deleteById, id: {}", id);

        if (!customerMongoDbRepository.existsById(id)) {
            log.error("Customer details not found for id: {}", id);
            throw new CustomerDetailsNotFoundException("Customer details not found");
        }

        customerMongoDbRepository.deleteById(id);
        log.info("Customer with id {} deleted successfully", id);
    }

    @Override
    public CustomerTO save(CustomerRequest customerRequest) throws CustomerDetailsNotFoundException {
        log.info("Inside CustomerServiceImpl.save, customerRequest: {}", customerRequest);

        Customer customer = new Customer();
        customer.setCustId(customerRequest.getCustId());
        customer.setCustName(customerRequest.getCustName());
        customer.setCustPhone(customerRequest.getCustPhone());
        customer.setCustAddress(customerRequest.getCustAddress());

        Customer savedCustomer = customerMongoDbRepository.save(customer);
        log.info("Customer saved: {}", savedCustomer);

        if (Objects.isNull(savedCustomer)) {
            log.error("Customer details not saved");
            throw new CustomerDetailsNotFoundException("Customer details not saved");
        }

        return new CustomerTO(savedCustomer.getId(), savedCustomer.getCustId(), savedCustomer.getCustName(), savedCustomer.getCustPhone(), savedCustomer.getCustAddress());
    }

    @Override
    public CustomerTO update(CustomerUpdateRequest customerRequest) throws CustomerDetailsNotFoundException {
        log.info("Inside CustomerServiceImpl.update, customerUpdateRequest: {}", customerRequest);

        Optional<Customer> customerOptional = customerMongoDbRepository.findById(customerRequest.getId());

        if (customerOptional.isEmpty()) {
            log.error("Customer details not found");
            throw new CustomerDetailsNotFoundException("Customer details not found");
        }

        Customer customer = customerOptional.get();
        if (customerRequest.getCustId() > 0) {
            customer.setCustId(customerRequest.getCustId());
        }
        if (customerRequest.getCustName() != null) {
            customer.setCustName(customerRequest.getCustName());
        }
        if (customerRequest.getCustPhone() > 0) {
            customer.setCustPhone(customerRequest.getCustPhone());
        }
        if (customerRequest.getCustAddress() != null) {
            customer.setCustAddress(customerRequest.getCustAddress());
        }

        Customer updatedCustomer = customerMongoDbRepository.save(customer);

        if (Objects.isNull(updatedCustomer)) {
            log.error("Customer details not updated");
            throw new CustomerDetailsNotFoundException("Customer details not updated");
        }

        return new CustomerTO(updatedCustomer.getId(), updatedCustomer.getCustId(), updatedCustomer.getCustName(), updatedCustomer.getCustPhone(), updatedCustomer.getCustAddress());
    }
}
