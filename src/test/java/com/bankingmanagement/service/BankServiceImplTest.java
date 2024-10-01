package com.bankingmanagement.service;

import com.bankingmanagement.document.Bank;
import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankRequest;
import com.bankingmanagement.model.BankTO;
import com.bankingmanagement.model.BankUpdateRequest;
import com.bankingmanagement.mongoRepository.BankMongoDbRepository;
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
public class BankServiceImplTest {
    @Mock
    private BankMongoDbRepository bankMongoDbRepository;

    @InjectMocks
    private BankServiceImpl bankService;

    @Test
    public void findAll_whenBankDetailsExist_theReturnBankData() throws BankDetailsNotFoundException {
        List<Bank> bankList = new ArrayList<>();
        Bank bank = new Bank();
        bank.setBankCode(234);
        bank.setBankName("SBI");
        bank.setBankAddress("Bangalore");
        bankList.add(bank);

        when(bankMongoDbRepository.findAll()).thenReturn(bankList);

        List<BankTO> bankTOS = bankService.findAll();
        assertEquals(1, bankTOS.size());
    }

    @Test
    public void findAll_whenBankDetailsNotExist_thenThrowException(){
        List<Bank> bankList = null;
        when(bankMongoDbRepository.findAll()).thenReturn(bankList);

        assertThrows(BankDetailsNotFoundException.class, ()-> bankService.findAll());
    }
    @Test
    public void findById_whenBankDetailsExist_thenReturnBankData() throws BankDetailsNotFoundException {
        List<Bank> bankById = new ArrayList<>();
        Bank bank = new Bank();
        bank.setId("123");
        bank.setBankCode(234);
        bank.setBankName("SBI");
        bank.setBankAddress("Bangalore");

        when(bankMongoDbRepository.findById("123")).thenReturn(Optional.of(bank));

        BankTO bankTO = bankService.findById("123");
        assertEquals("123", bankTO.id());
        assertEquals(234, bankTO.bankCode());
        assertEquals("SBI", bankTO.bankName());
    }
    @Test
    public void findById_whenBankDetailsNotExist_thenThrowException() {
        when(bankMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(BankDetailsNotFoundException.class, () -> bankService.findById("123"));
    }
    @Test
    public void findByBankName_whenBankDetailsExist_thenReturnBankData() throws BankDetailsNotFoundException {
        Bank bank = new Bank();
        bank.setId("123");
        bank.setBankCode(234);
        bank.setBankName("SBI");
        bank.setBankAddress("Bangalore");

        when(bankMongoDbRepository.findByBankName("SBI")).thenReturn(bank);

        BankTO bankTO = bankService.findByBankName("SBI");
        assertEquals("123", bankTO.id());
        assertEquals(234, bankTO.bankCode());
        assertEquals("SBI", bankTO.bankName());
    }

    @Test
    public void findByBankName_whenBankDetailsNotExist_thenThrowException() {
        when(bankMongoDbRepository.findByBankName("SBI")).thenReturn(null);

        assertThrows(BankDetailsNotFoundException.class, () -> bankService.findByBankName("SBI"));
    }
    @Test
    public void deleteById_whenBankExists_thenDeleteSuccessfully() throws BankDetailsNotFoundException {
        when(bankMongoDbRepository.existsById("123")).thenReturn(true);

        bankService.deleteById("123");

        verify(bankMongoDbRepository).deleteById("123");
    }

    @Test
    public void deleteById_whenBankNotExist_thenThrowException() {
        when(bankMongoDbRepository.existsById("123")).thenReturn(false);

        assertThrows(BankDetailsNotFoundException.class, () -> bankService.deleteById("123"));
    }
    @Test
    public void save_whenBankDetailsAreValid_thenReturnSavedBank() throws BankDetailsNotFoundException {
        BankRequest bankRequest = new BankRequest();
        Bank bank = new Bank();
        bank.setId("123");
        bank.setBankCode(234);
        bank.setBankName("SBI");
        bank.setBankAddress("Bangalore");

        when(bankMongoDbRepository.save(any(Bank.class))).thenReturn(bank);

        BankTO bankTO = bankService.save(bankRequest);

        assertEquals("123", bankTO.id());
        assertEquals(234, bankTO.bankCode());
        assertEquals("SBI", bankTO.bankName());
    }

    @Test
    public void save_whenBankNotSaved_thenThrowException() {
        BankRequest bankRequest = new BankRequest( );

        when(bankMongoDbRepository.save(any(Bank.class))).thenReturn(null);

        assertThrows(BankDetailsNotFoundException.class, () -> bankService.save(bankRequest));
    }
    @Test
    public void update_whenValidUpdateRequest_thenBankIsUpdated() throws BankDetailsNotFoundException {
        // Arrange
        Bank bank = new Bank();
        bank.setId("123");
        bank.setBankCode(234);
        bank.setBankName("SBI");
        bank.setBankAddress("Bangalore");

        BankUpdateRequest updateRequest = new BankUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setBankCode(567);
        updateRequest.setBankName("ICICI");
        updateRequest.setBankAddress("Mumbai");

        when(bankMongoDbRepository.findById("123")).thenReturn(Optional.of(bank));
        when(bankMongoDbRepository.save(bank)).thenReturn(bank);


        BankTO bankTO = bankService.update(updateRequest);


        assertEquals("123", bankTO.id());
        assertEquals(567, bankTO.bankCode());
        assertEquals("ICICI", bankTO.bankName());
        assertEquals("Mumbai", bankTO.bankAddress());
    }

    @Test
    public void update_whenBankDoesNotExist_thenThrowException() {

        BankUpdateRequest updateRequest = new BankUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setBankCode(567);
        updateRequest.setBankName("ICICI");
        updateRequest.setBankAddress("Mumbai");

        when(bankMongoDbRepository.findById("123")).thenReturn(Optional.empty());


        assertThrows(BankDetailsNotFoundException.class, () -> bankService.update(updateRequest));
    }

}
