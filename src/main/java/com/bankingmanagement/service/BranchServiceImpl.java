package com.bankingmanagement.service;

import com.bankingmanagement.document.Branch;
import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchRequest;
import com.bankingmanagement.model.BranchTO;
import com.bankingmanagement.model.BranchUpdateRequest;
import com.bankingmanagement.mongoRepository.BranchMongoDbRepository;
import com.bankingmanagement.repository.BankRepository;
import com.bankingmanagement.repository.BranchRepository;
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

        return new BranchTO(branch.getId(), branch.getBranchId(), branch.getBranchAddress(), branch.getBranchName(), branch.getBranchCode());
    }

    @Override
    public BranchTO findByBranchName(String branchName) throws BranchDetailsNotFoundException {
        log.info("Inside the BranchServiceImpl.findByBranchName, branchName:{}", branchName);
        Branch branch = branchMongoDbRepository.findByBranchName(branchName);

        if (Objects.isNull(branch)) {
            log.error("Branch details not found for branch name:{}", branchName);
            throw new BranchDetailsNotFoundException("Branch details not found");
        }

        log.info("Branch details:{}", branch);

        BranchTO branchTO = new BranchTO(branch.getId(), branch.getBranchId(), branch.getBranchAddress(), branch.getBranchName(), branch.getBranchCode());
        return branchTO;
    }
    @Override
    public void deleteById(String id) throws BranchDetailsNotFoundException {
        if (!branchMongoDbRepository.existsById(id)) {
            log.error("Branch details not found for id: {}", id);
            throw new BranchDetailsNotFoundException("Branch details not found");
        }
        branchMongoDbRepository.deleteById(id);
        log.info("Branch with id {} deleted successfully", id);
    }

    @Override
    public BranchTO save(BranchRequest branchRequest) throws BranchDetailsNotFoundException {
        Branch branch = new Branch();
        branch.setBranchId(branchRequest.getBranchId());
        branch.setBranchAddress(branchRequest.getBranchAddress());
        branch.setBranchName(branchRequest.getBranchName());
        branch.setBranchCode(branchRequest.getBranchCode());
        Branch savedBranch = branchMongoDbRepository.save(branch);

        if (Objects.isNull(savedBranch)) {
            log.error("Branch details not saved");
            throw new BranchDetailsNotFoundException("Branch details not found");
        }

        return new BranchTO(savedBranch.getId(), savedBranch.getBranchId(), savedBranch.getBranchAddress(), savedBranch.getBranchName(), savedBranch.getBranchCode());
    }

    @Override
    public BranchTO update(BranchUpdateRequest branchRequest) throws BranchDetailsNotFoundException {
        Optional<Branch> branchOptional = branchMongoDbRepository.findById(branchRequest.getId());
        if (branchOptional.isEmpty()) {
            log.error("Branch details not found");
            throw new BranchDetailsNotFoundException("Branch details not found");
        }

        Branch branch = branchOptional.get();
        if (branchRequest.getBranchId() > 0) {
            branch.setBranchId(branchRequest.getBranchId());
        }
        if (branchRequest.getBranchAddress() != null) {
            branch.setBranchAddress(branchRequest.getBranchAddress());
        }
        if (branchRequest.getBranchName() != null) {
            branch.setBranchName(branchRequest.getBranchName());
        }
        if (branchRequest.getBranchCode() > 0) {
            branch.setBranchCode(branchRequest.getBranchCode());
        }

        Branch updatedBranch = branchMongoDbRepository.save(branch);
        if (Objects.isNull(updatedBranch)) {
            log.error("Branch details not updated");
            throw new BranchDetailsNotFoundException("Branch details not updated");
        }

        return new BranchTO(updatedBranch.getId(), updatedBranch.getBranchId(), updatedBranch.getBranchAddress(), updatedBranch.getBranchName(), updatedBranch.getBranchCode());
    }
}
