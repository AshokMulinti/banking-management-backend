package com.bankingmanagement.service;

import com.bankingmanagement.document.Loan;
import com.bankingmanagement.exceptions.LoanDetailsNotFoundException;
import com.bankingmanagement.model.LoanRequest;
import com.bankingmanagement.model.LoanTO;
import com.bankingmanagement.model.LoanUpdateRequest;
import com.bankingmanagement.mongoRepository.LoanMongoDbRepository;
import com.bankingmanagement.repository.LoanRepository;
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
public class LoanServiceImplTest {
    @Mock
    private LoanMongoDbRepository loanMongoDbRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void findAll_whenLoanDetailsExist_theReturnLoanData() throws LoanDetailsNotFoundException {
        List<Loan> loanList = new ArrayList<>();
        Loan loan = new Loan();
        loan.setId("123");
        loan.setLoanId(1001);
        loan.setLoanType("Personal");
        loan.setLoanAmount(50000);
        loan.setBranchId(1);
        loan.setCustId(1);
        loanList.add(loan);

        when(loanMongoDbRepository.findAll()).thenReturn(loanList);

        List<LoanTO> loanTOS = loanService.findAll();
        assertEquals(1, loanTOS.size());
    }

    @Test
    public void findAll_whenLoanDetailsNotExist_thenThrowException() {
        when(loanMongoDbRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(LoanDetailsNotFoundException.class, () -> loanService.findAll());
    }

    @Test
    public void findById_whenLoanDetailsExist_thenReturnLoanData() throws LoanDetailsNotFoundException {
        Loan loan = new Loan();
        loan.setId("123");
        loan.setLoanId(1001);
        loan.setLoanType("Personal");
        loan.setLoanAmount(50000);
        loan.setBranchId(1);
        loan.setCustId(1);

        when(loanMongoDbRepository.findById("123")).thenReturn(Optional.of(loan));

        LoanTO loanTO = loanService.findById("123");
        assertEquals("123", loanTO.id());
        assertEquals(1001, loanTO.loanId());
        assertEquals("Personal", loanTO.loanType());
    }

    @Test
    public void findById_whenLoanDetailsNotExist_thenThrowException() {
        when(loanMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(LoanDetailsNotFoundException.class, () -> loanService.findById("123"));
    }

    @Test
    public void findByLoanType_whenLoanDetailsExist_thenReturnLoanData() throws LoanDetailsNotFoundException {
        Loan loan = new Loan();
        loan.setId("123");
        loan.setLoanId(1001);
        loan.setLoanType("Personal");
        loan.setLoanAmount(50000);
        loan.setBranchId(1);
        loan.setCustId(1);

        when(loanMongoDbRepository.findByLoanType("Personal")).thenReturn(loan);

        LoanTO loanTO = loanService.findByLoanType("Personal");
        assertEquals("123", loanTO.id());
        assertEquals(1001, loanTO.loanId());
        assertEquals("Personal", loanTO.loanType());
    }

    @Test
    public void findByLoanType_whenLoanDetailsNotExist_thenThrowException() {
        when(loanMongoDbRepository.findByLoanType("Personal")).thenReturn(null);

        assertThrows(LoanDetailsNotFoundException.class, () -> loanService.findByLoanType("Personal"));
    }

    @Test
    public void deleteById_whenLoanExists_thenDeleteSuccessfully() throws LoanDetailsNotFoundException {
        when(loanMongoDbRepository.existsById("123")).thenReturn(true);

        loanService.deleteById("123");

        verify(loanMongoDbRepository).deleteById("123");
    }

    @Test
    public void deleteById_whenLoanNotExist_thenThrowException() {
        when(loanMongoDbRepository.existsById("123")).thenReturn(false);

        assertThrows(LoanDetailsNotFoundException.class, () -> loanService.deleteById("123"));
    }

    @Test
    public void save_whenLoanDetailsAreValid_thenReturnSavedLoan() throws LoanDetailsNotFoundException {
        LoanRequest loanRequest = new LoanRequest();
        Loan loan = new Loan();
        loan.setId("123");
        loan.setLoanId(1001);
        loan.setLoanType("Personal");
        loan.setLoanAmount(50000);
        loan.setBranchId(1);
        loan.setCustId(1);

        when(loanMongoDbRepository.save(any(Loan.class))).thenReturn(loan);

        LoanTO loanTO = loanService.save(loanRequest);

        assertEquals("123", loanTO.id());
        assertEquals(1001, loanTO.loanId());
        assertEquals("Personal", loanTO.loanType());
    }

    @Test
    public void save_whenLoanNotSaved_thenThrowException() {
        LoanRequest loanRequest = new LoanRequest();

        when(loanMongoDbRepository.save(any(Loan.class))).thenReturn(null);

        assertThrows(LoanDetailsNotFoundException.class, () -> loanService.save(loanRequest));
    }

    @Test
    public void update_whenValidUpdateRequest_thenLoanIsUpdated() throws LoanDetailsNotFoundException {
        Loan loan = new Loan();
        loan.setId("123");
        loan.setLoanId(1001);
        loan.setLoanType("Personal");
        loan.setLoanAmount(50000);
        loan.setBranchId(1);
        loan.setCustId(1);

        LoanUpdateRequest updateRequest = new LoanUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setLoanId(2002);
        updateRequest.setLoanType("Home");
        updateRequest.setLoanAmount(60000);
        updateRequest.setBranchId(2);
        updateRequest.setCustId(2);

        when(loanMongoDbRepository.findById("123")).thenReturn(Optional.of(loan));
        when(loanMongoDbRepository.save(loan)).thenReturn(loan);

        LoanTO loanTO = loanService.update(updateRequest);

        assertEquals("123", loanTO.id());
        assertEquals(2002, loanTO.loanId());
        assertEquals("Home", loanTO.loanType());
        assertEquals(60000, loanTO.loanAmount());
    }

    @Test
    public void update_whenLoanDoesNotExist_thenThrowException() {
        LoanUpdateRequest updateRequest = new LoanUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setLoanId(2002);
        updateRequest.setLoanType("Home");
        updateRequest.setLoanAmount(60000);
        updateRequest.setBranchId(2);
        updateRequest.setCustId(2);

        when(loanMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(LoanDetailsNotFoundException.class, () -> loanService.update(updateRequest));
    }
}
