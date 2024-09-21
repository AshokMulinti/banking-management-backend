package com.bankingmanagement.service;

import com.bankingmanagement.document.Loan;
import com.bankingmanagement.exceptions.LoanDetailsNotFoundException;
import com.bankingmanagement.model.LoanTO;
import com.bankingmanagement.mongoRepository.LoanMongoDbRepository;
import com.bankingmanagement.repository.BankRepository;
import com.bankingmanagement.repository.LoanRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
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
}
