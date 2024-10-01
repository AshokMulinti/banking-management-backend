package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchRequest;
import com.bankingmanagement.model.BranchTO;
import com.bankingmanagement.model.BranchUpdateRequest;

import java.util.List;

public interface BranchService {
    List<BranchTO> findAll() throws BranchDetailsNotFoundException;
    BranchTO findById(String id) throws BranchDetailsNotFoundException;
    BranchTO findByBranchName(String branchName) throws BranchDetailsNotFoundException;
    void deleteById(String id) throws BranchDetailsNotFoundException;
    BranchTO save(BranchRequest branchRequest) throws BranchDetailsNotFoundException;
    BranchTO update(BranchUpdateRequest branchRequest) throws BranchDetailsNotFoundException;
}

