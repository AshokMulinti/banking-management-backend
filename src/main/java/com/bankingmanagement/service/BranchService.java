package com.bankingmanagement.service;

import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchTO;

import java.util.List;

public interface BranchService {
    List<BranchTO> findAll() throws BranchDetailsNotFoundException;
    BranchTO findById(String id) throws BranchDetailsNotFoundException;
}
