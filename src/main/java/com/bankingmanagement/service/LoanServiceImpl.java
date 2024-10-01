package com.bankingmanagement.service;

import com.bankingmanagement.document.Loan;
import com.bankingmanagement.exceptions.LoanDetailsNotFoundException;
import com.bankingmanagement.model.LoanRequest;
import com.bankingmanagement.model.LoanTO;
import com.bankingmanagement.model.LoanUpdateRequest;
import com.bankingmanagement.mongoRepository.LoanMongoDbRepository;
import com.bankingmanagement.repository.BankRepository;
import com.bankingmanagement.repository.LoanRepository;
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
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private LoanMongoDbRepository loanMongoDbRepository;

    @Override
    public List<LoanTO> findAll() throws LoanDetailsNotFoundException {
        log.info("Inside LoanServiceImpl.findAll");
        List<Loan> loanList = loanMongoDbRepository.findAll();
        if (CollectionUtils.isEmpty(loanList)) {
            log.error("Loan details not found");
            throw new LoanDetailsNotFoundException("Loan details not found");
        }
        List<LoanTO> loanTOS = loanList.stream().map(loan -> {
            return new LoanTO(loan.getId(), loan.getLoanId(), loan.getLoanType(), loan.getLoanAmount(), loan.getBranchId(), loan.getCustId());
        }).collect(Collectors.toList());
        log.info("Loan details:{}", loanTOS);
        return loanTOS;
    }
    @Override
    public LoanTO findById(String id) throws LoanDetailsNotFoundException {
        log.info("Inside the LoanServiceImpl.findById, id:{}", id);
        Optional<Loan> loanOptional = loanMongoDbRepository.findById(id);

        if (loanOptional.isEmpty()) {
            log.error("Loan details not found for id: {}", id);
            throw new LoanDetailsNotFoundException("Loan details not found");
        }

        Loan loan = loanOptional.get();
        log.info("Loan details:{}", loan);

        return new LoanTO(loan.getId(), loan.getLoanId(), loan.getLoanType(), loan.getLoanAmount(), loan.getBranchId(), loan.getCustId());
    }

    @Override
    public LoanTO findByLoanType(String loanType) throws LoanDetailsNotFoundException {
        log.info("Inside the LoanServiceImpl.findByLoanType, loanType:{}", loanType);
        Loan loan = loanMongoDbRepository.findByLoanType(loanType);

        if (Objects.isNull(loan)) {
            log.error("Loan details not found for loan type:{}", loanType);
            throw new LoanDetailsNotFoundException("Loan details not found");
        }

        log.info("Loan details:{}", loan);

        LoanTO loanTO = new LoanTO(loan.getId(), loan.getLoanId(), loan.getLoanType(), loan.getLoanAmount(), loan.getBranchId(), loan.getCustId());
        return loanTO;
    }
    @Override
    public void deleteById(String id) throws LoanDetailsNotFoundException {
        log.info("Inside LoanServiceImpl.deleteById, id: {}", id);
        if (!loanMongoDbRepository.existsById(id)) {
            throw new LoanDetailsNotFoundException("Loan details not found for id: " + id);
        }
        loanMongoDbRepository.deleteById(id);
        log.info("Loan with id {} deleted successfully", id);
    }

    @Override
    public LoanTO save(LoanRequest loanRequest) throws LoanDetailsNotFoundException {
        log.info("Inside LoanServiceImpl.save, loanRequest: {}", loanRequest);
        Loan loan = new Loan();
        loan.setLoanId(loanRequest.getLoanId());
        loan.setLoanType(loanRequest.getLoanType());
        loan.setLoanAmount(loanRequest.getLoanAmount());
        loan.setBranchId(loanRequest.getBranchId());
        loan.setCustId(loanRequest.getCustId());

        Loan loanResponse = loanMongoDbRepository.save(loan);
        if (Objects.isNull(loanResponse)) {
            throw new LoanDetailsNotFoundException("Loan details not saved");
        }
        return new LoanTO(loanResponse.getId(), loanResponse.getLoanId(), loanResponse.getLoanType(), loanResponse.getLoanAmount(), loanResponse.getBranchId(), loanResponse.getCustId());
    }

    @Override
    public LoanTO update(LoanUpdateRequest loanRequest) throws LoanDetailsNotFoundException {
        log.info("Inside LoanServiceImpl.update, loanRequest: {}", loanRequest);
        Optional<Loan> loanOptional = loanMongoDbRepository.findById(loanRequest.getId());
        if (loanOptional.isEmpty()) {
            throw new LoanDetailsNotFoundException("Loan details not found for id: " + loanRequest.getId());
        }
        Loan loan = loanOptional.get();
        if (loanRequest.getLoanId() > 0) {
            loan.setLoanId(loanRequest.getLoanId());
        }
        if (loanRequest.getLoanType() != null) {
            loan.setLoanType(loanRequest.getLoanType());
        }
        if (loanRequest.getLoanAmount() > 0) {
            loan.setLoanAmount(loanRequest.getLoanAmount());
        }
        if (loanRequest.getBranchId() > 0) {
            loan.setBranchId(loanRequest.getBranchId());
        }
        if (loanRequest.getCustId() > 0) {
            loan.setCustId(loanRequest.getCustId());
        }
        Loan updateResponse = loanMongoDbRepository.save(loan);
        if (Objects.isNull(updateResponse)) {
            throw new LoanDetailsNotFoundException("Loan details not updated");
        }
        return new LoanTO(updateResponse.getId(), updateResponse.getLoanId(), updateResponse.getLoanType(), updateResponse.getLoanAmount(), updateResponse.getBranchId(), updateResponse.getCustId());
    }
}
