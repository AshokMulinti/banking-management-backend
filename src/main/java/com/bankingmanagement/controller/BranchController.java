package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchTO;
import com.bankingmanagement.service.BankService;
import com.bankingmanagement.service.BranchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {
    @Autowired
    private BranchService branchService;
    @GetMapping()
    public ResponseEntity<List<BranchTO>> findAll() {
        log.info("Inside the BranchController.findAllBranches");

        List<BranchTO> branchTOS;
        try {
            branchTOS = branchService.findAll();
            log.info("Branch details:{}", branchTOS);
        } catch (BranchDetailsNotFoundException ex) {
            log.error("Branch details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting branch details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of BranchController.findAllBranches");
        return new ResponseEntity<>(branchTOS, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BranchTO> findById(@PathVariable("id") String id) {
        log.info("Inside the BranchController.findById, id:{}", id);
        BranchTO branch;
        try {
            branch = branchService.findById(id);
            log.info("Branch details: {}", branch);
        } catch (BranchDetailsNotFoundException ex) {
            log.error("Branch details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting branch details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of BranchController.findById");
        return new ResponseEntity<>(branch, HttpStatus.OK);
    }
}
