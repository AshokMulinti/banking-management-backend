package com.bankingmanagement.service;
import com.bankingmanagement.document.Customer;
import com.bankingmanagement.exceptions.CustomerDetailsNotFoundException;
import com.bankingmanagement.model.CustomerRequest;
import com.bankingmanagement.model.CustomerTO;
import com.bankingmanagement.model.CustomerUpdateRequest;
import com.bankingmanagement.mongoRepository.CustomerMongoDbRepository;
import com.bankingmanagement.repository.CustomerRepository;
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
public class CustomerServiceImplTest {
    @Mock
    private CustomerMongoDbRepository customerMongoDbRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void findAll_whenCustomerDetailsExist_thenReturnCustomerData() throws CustomerDetailsNotFoundException {
        List<Customer> customerList = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId("123");
        customer.setCustId(1);
        customer.setCustName("John Doe");
        customer.setCustPhone(1234567890);
        customer.setCustAddress("123 Street, City");
        customerList.add(customer);

        when(customerMongoDbRepository.findAll()).thenReturn(customerList);

        List<CustomerTO> customerTOS = customerService.findAll();
        assertEquals(1, customerTOS.size());
    }

    @Test
    public void findAll_whenCustomerDetailsNotExist_thenThrowException() {
        when(customerMongoDbRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(CustomerDetailsNotFoundException.class, () -> customerService.findAll());
    }

    @Test
    public void findById_whenCustomerDetailsExist_thenReturnCustomerData() throws CustomerDetailsNotFoundException {
        Customer customer = new Customer();
        customer.setId("123");
        customer.setCustId(1);
        customer.setCustName("John Doe");
        customer.setCustPhone(1234567890);
        customer.setCustAddress("123 Street, City");

        when(customerMongoDbRepository.findById("123")).thenReturn(Optional.of(customer));

        CustomerTO customerTO = customerService.findById("123");
        assertEquals("123", customerTO.id());
        assertEquals(1, customerTO.custId());
        assertEquals("John Doe", customerTO.custName());
    }

    @Test
    public void findById_whenCustomerDetailsNotExist_thenThrowException() {
        when(customerMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(CustomerDetailsNotFoundException.class, () -> customerService.findById("123"));
    }

    @Test
    public void findByCustName_whenCustomerDetailsExist_thenReturnCustomerData() throws CustomerDetailsNotFoundException {
        Customer customer = new Customer();
        customer.setId("123");
        customer.setCustId(1);
        customer.setCustName("John Doe");
        customer.setCustPhone(1234567890);
        customer.setCustAddress("123 Street, City");

        when(customerMongoDbRepository.findByCustName("John Doe")).thenReturn(customer);

        CustomerTO customerTO = customerService.findByCustName("John Doe");
        assertEquals("123", customerTO.id());
        assertEquals(1, customerTO.custId());
        assertEquals("John Doe", customerTO.custName());
    }

    @Test
    public void findByCustName_whenCustomerDetailsNotExist_thenThrowException() {
        when(customerMongoDbRepository.findByCustName("John Doe")).thenReturn(null);

        assertThrows(CustomerDetailsNotFoundException.class, () -> customerService.findByCustName("John Doe"));
    }

    @Test
    public void deleteById_whenCustomerExists_thenDeleteSuccessfully() throws CustomerDetailsNotFoundException {
        when(customerMongoDbRepository.existsById("123")).thenReturn(true);

        customerService.deleteById("123");

        verify(customerMongoDbRepository).deleteById("123");
    }

    @Test
    public void deleteById_whenCustomerNotExist_thenThrowException() {
        when(customerMongoDbRepository.existsById("123")).thenReturn(false);

        assertThrows(CustomerDetailsNotFoundException.class, () -> customerService.deleteById("123"));
    }

    @Test
    public void save_whenCustomerDetailsAreValid_thenReturnSavedCustomer() throws CustomerDetailsNotFoundException {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setCustId(1);
        customerRequest.setCustName("John Doe");
        customerRequest.setCustPhone(1234567890);
        customerRequest.setCustAddress("123 Street, City");

        Customer customer = new Customer();
        customer.setId("123");
        customer.setCustId(1);
        customer.setCustName("John Doe");
        customer.setCustPhone(1234567890);
        customer.setCustAddress("123 Street, City");

        when(customerMongoDbRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerTO customerTO = customerService.save(customerRequest);

        assertEquals("123", customerTO.id());
        assertEquals(1, customerTO.custId());
        assertEquals("John Doe", customerTO.custName());
    }

    @Test
    public void save_whenCustomerNotSaved_thenThrowException() {
        CustomerRequest customerRequest = new CustomerRequest();

        when(customerMongoDbRepository.save(any(Customer.class))).thenReturn(null);

        assertThrows(CustomerDetailsNotFoundException.class, () -> customerService.save(customerRequest));
    }

    @Test
    public void update_whenValidUpdateRequest_thenCustomerIsUpdated() throws CustomerDetailsNotFoundException {
        Customer customer = new Customer();
        customer.setId("123");
        customer.setCustId(1);
        customer.setCustName("John Doe");
        customer.setCustPhone(123456);
        customer.setCustAddress("123 Street, City");

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setCustId(2);
        updateRequest.setCustName("Jane Doe");
        updateRequest.setCustPhone(987654);
        updateRequest.setCustAddress("456 Avenue, City");

        when(customerMongoDbRepository.findById("123")).thenReturn(Optional.of(customer));
        when(customerMongoDbRepository.save(customer)).thenReturn(customer);

        CustomerTO customerTO = customerService.update(updateRequest);

        assertEquals("123", customerTO.id());
        assertEquals(2, customerTO.custId());
        assertEquals("Jane Doe", customerTO.custName());
        assertEquals(987654, customerTO.custPhone());
        assertEquals("456 Avenue, City", customerTO.custAddress());
    }

    @Test
    public void update_whenCustomerDoesNotExist_thenThrowException() {
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setCustId(2);
        updateRequest.setCustName("Jane Doe");
        updateRequest.setCustPhone(987654);
        updateRequest.setCustAddress("456 Avenue, City");

        when(customerMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(CustomerDetailsNotFoundException.class, () -> customerService.update(updateRequest));
    }
}
