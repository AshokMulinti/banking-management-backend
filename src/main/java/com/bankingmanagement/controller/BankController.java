package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankTO;
import com.bankingmanagement.service.BankService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1/banks")
public class BankController {
    @Autowired
    private BankService bankService;

    @GetMapping()
    public ResponseEntity<List<BankTO>> findAll(){
     log.info("Inside the BankController.findAllBanks");

     List<BankTO> bankTOS;
     try{
         bankTOS = bankService.findAll();
         log.info("Bank details:{}",bankTOS);
     }catch(BankDetailsNotFoundException ex){
         log.error("Bank derails not found",ex);
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }catch(Exception ex1){
         log.error("Exception while getting bank details",ex1);
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
     log.info("End of BankController.findAllBanks");
     return new ResponseEntity<>(bankTOS,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BankTO> findById(@PathVariable("id") String id ){
        log.info("Inside the BankController.findById, id:{}", id);
        BankTO bank = null;
        try{
            bank = bankService.findById(id);
            log.info("Bank details, bank:{}", bank);
        }catch (BankDetailsNotFoundException ex){
            log.error("Bank details not found", ex);
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1){
            log.error("Exception while getting the bank details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of BankController.findById");
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }
}
