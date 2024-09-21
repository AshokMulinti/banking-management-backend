package com.bankingmanagement.service;

import com.bankingmanagement.document.Branch;
import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchTO;
import com.bankingmanagement.mongoRepository.BranchMongoDbRepository;
import com.bankingmanagement.repository.BankRepository;
import com.bankingmanagement.repository.BranchRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BranchServiceImpl implements BranchService{
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BranchMongoDbRepository branchMongoDbRepository;
    @Override
    public List<BranchTO> findAll() throws BranchDetailsNotFoundException {
        log.info("Inside the BranchServiceImpl.findAll");
        List<Branch> branchList = branchMongoDbRepository.findAll();
        if (CollectionUtils.isEmpty(branchList)) {
            log.error("Branch details not found");
            throw new BranchDetailsNotFoundException("Branch details not found");
        }
        List<BranchTO> branchTOS = branchList.stream().map(branch -> {
            return new BranchTO(branch.getId(), branch.getBranchId(), branch.getBranchAddress(), branch.getBranchName(), branch.getBranchCode());
        }).collect(Collectors.toList());
        log.info("Branch details:{}", branchTOS);
        return branchTOS;
    }
    @Override
    public BranchTO findById(String id) throws BranchDetailsNotFoundException {
        log.info("Inside the BranchServiceImpl.findById, id:{}", id);
        Optional<Branch> branchOptional = branchMongoDbRepository.findById(id);

        if (branchOptional.isEmpty()) {
            log.error("Branch details not found for id: {}", id);
            throw new BranchDetailsNotFoundException("Branch details not found");
        }

        Branch branch = branchOptional.get();
        log.info("Branch details:{}", branch);

        return new BranchTO(branch.getId(), branch.getBranchId(), branch.getBranchName(), branch.getBranchAddress(), branch.getBranchCode());
    }
}
