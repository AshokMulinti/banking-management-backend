package com.bankingmanagement.service;

import com.bankingmanagement.document.Bank;
import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankTO;
import com.bankingmanagement.mongoRepository.BankMongoDbRepository;
import com.bankingmanagement.repository.BankRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BankServiceImpl implements BankService{
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private BankMongoDbRepository bankMongoDbRepository;

    @Override
    public List<BankTO> findAll() throws BankDetailsNotFoundException {
        log.info("Inside the BankServiceImpl.findAll");
        List<Bank> bankList = bankMongoDbRepository.findAll();

        if(CollectionUtils.isEmpty(bankList)){
            log.error("Bank details not Fount");
            throw new BankDetailsNotFoundException("Bank details not Fount");
        }
        List<BankTO> bankTOS = bankList.stream().map(bank->{
           BankTO bankTO = new BankTO(bank.getId(),bank.getBankCode(),bank.getBankName(),bank.getBankAddress());
           return bankTO;
        }).collect(Collectors.toList());
         log.info("Bank details:{}",bankTOS);
        return bankTOS;
    }
    //find bank details by id;
    @Override
    public BankTO findById(String id) throws BankDetailsNotFoundException {
        log.info("Inside the BankServiceImpl.findById, id:{}", id);
        Optional<Bank> bankOptional = bankMongoDbRepository.findById(id);

        if(bankOptional.isEmpty()){
            log.error("Bank details not found for the bank id:{}", id);
            throw new BankDetailsNotFoundException("Bank details not found");
        }

        Bank bank = bankOptional.get();
        log.info("Bank details:{}", bank);

        BankTO bankTO = new BankTO(bank.getId(),bank.getBankCode(), bank.getBankName(), bank.getBankAddress());
        return bankTO;
    }


}
