package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchRequest;
import com.bankingmanagement.model.BranchTO;
import com.bankingmanagement.model.BranchUpdateRequest;
import com.bankingmanagement.service.BankService;
import com.bankingmanagement.service.BranchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/branchname")
    public ResponseEntity<BranchTO> findByBranchName(@RequestParam("branchName") String branchName) {
        log.info("Inside the BranchController.findByBranchName, branchName:{}", branchName);
        BranchTO branch = null;
        try {
            branch = branchService.findByBranchName(branchName);
            log.info("Branch details, branch:{}", branch);
        } catch (BranchDetailsNotFoundException ex) {
            log.error("Branch details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while getting the branch details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of BranchController.findByBranchName");
        return new ResponseEntity<>(branch, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<BranchTO> save(@RequestBody BranchRequest branchRequest) {
        log.info("Inside BranchController.save, branchRequest: {}", branchRequest);
        BranchTO branchTO;
        try {
            branchTO = branchService.save(branchRequest);
        } catch (BranchDetailsNotFoundException e) {
            log.error("Error saving branch details", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(branchTO, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<BranchTO> update(@RequestBody BranchUpdateRequest branchUpdateRequest) {
        log.info("Inside BranchController.update, branchUpdateRequest: {}", branchUpdateRequest);
        BranchTO branchTO;
        try {
            branchTO = branchService.update(branchUpdateRequest);
        } catch (BranchDetailsNotFoundException e) {
            log.error("Branch details not found", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(branchTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        log.info("Inside BranchController.deleteById, id: {}", id);
        try {
            branchService.deleteById(id);
        } catch (BranchDetailsNotFoundException e) {
            log.error("Branch details not found for id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
