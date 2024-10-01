package com.bankingmanagement.service;

import com.bankingmanagement.document.Bank;
import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankRequest;
import com.bankingmanagement.model.BankTO;
import com.bankingmanagement.model.BankUpdateRequest;
import com.bankingmanagement.mongoRepository.BankMongoDbRepository;
import com.bankingmanagement.repository.BankRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
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

    @Override
    public BankTO findByBankName(String name) throws BankDetailsNotFoundException {
        log.info("Inside the BankServiceImpl.findByBankName, name:{}", name);
        Bank bank = bankMongoDbRepository.findByBankName(name);

        if(Objects.isNull(bank)){
            log.error("Bank details not found for the bank id:{}", name);
            throw new BankDetailsNotFoundException("Bank details not found");
        }
        log.info("Bank details:{}", bank);

        BankTO bankTO = new BankTO(bank.getId(),bank.getBankCode(), bank.getBankName(), bank.getBankAddress());
        return bankTO;
    }

    @Override
    public void deleteById(String id) throws BankDetailsNotFoundException {
        log.info("Inside BankServiceImpl.deleteById, id: {}", id);

        if (!bankMongoDbRepository.existsById(id)) {
            log.error("Bank details not found for the bank id: {}", id);
            throw new BankDetailsNotFoundException("Bank details not found");
        }

        bankMongoDbRepository.deleteById(id);
        log.info("Bank with id {} deleted successfully", id);
    }
    @Override
    public BankTO save(BankRequest bankRequest) throws BankDetailsNotFoundException {
        log.info("Inside the BankServiceImpl.save,bankRequest:{} ", bankRequest);

        Bank bank = new Bank();
        bank.setBankCode(bankRequest.getBankCode());
        bank.setBankName(bankRequest.getBankName());
        bank.setBankAddress(bankRequest.getBankAddress());
        Bank bankResponse = bankMongoDbRepository.save(bank);
        log.info("Bank Response:{}", bankResponse);

        if(Objects.isNull(bankResponse)){
            log.error("Bank details not saved");
            throw new BankDetailsNotFoundException("Bank details not found");
        }
        BankTO bankTO = new BankTO(bankResponse.getId(), bankResponse.getBankCode(), bankResponse.getBankName(), bankResponse.getBankAddress());

        log.info("End of BankServiceImpl.save");
        return bankTO;
    }

    @Override
    public BankTO update(BankUpdateRequest bankRequest) throws BankDetailsNotFoundException {
        log.info("Inside the BankServiceImpl.update, bankUpdateRequest:{}", bankRequest);

        Optional<Bank> bankOptional = bankMongoDbRepository.findById(bankRequest.getId());

        if(bankOptional.isEmpty()) {
            log.error("Bank details not found");
            throw new BankDetailsNotFoundException("Bank details not found");
        }

        Bank bank = bankOptional.get();
        if(bankRequest.getBankCode()>0) {
            bank.setBankCode(bankRequest.getBankCode());
        }
        if(bankRequest.getBankAddress() != null) {
            bank.setBankAddress(bankRequest.getBankAddress());
        }

        if(bankRequest.getBankName() != null) {
            bank.setBankName(bankRequest.getBankName());
        }

        Bank updateResponse = bankMongoDbRepository.save(bank);
        if(Objects.isNull(updateResponse)){
            log.error("Bank details not update");
            throw new BankDetailsNotFoundException("Bank details not updated");
        }
        BankTO bankTO = new BankTO(updateResponse.getId(), updateResponse.getBankCode(), updateResponse.getBankName(), updateResponse.getBankAddress());

        log.info("End of BankServiceImpl.update");
        return bankTO;
    }
}
