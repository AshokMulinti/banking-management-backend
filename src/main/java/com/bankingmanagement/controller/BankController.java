package com.bankingmanagement.controller;

import com.bankingmanagement.exceptions.BankDetailsNotFoundException;
import com.bankingmanagement.model.BankRequest;
import com.bankingmanagement.model.BankTO;
import com.bankingmanagement.model.BankUpdateRequest;
import com.bankingmanagement.service.BankService;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/bankname")
    public ResponseEntity<BankTO> findByBankName(@RequestParam("bankName") String bankName ){
        log.info("Inside the BankController.findById, bankName:{}", bankName);
        BankTO bank = null;
        try{
            bank = bankService.findByBankName(bankName);
            log.info("Bank details, bank:{}", bank);
        }catch (BankDetailsNotFoundException ex){
            log.error("Bank details not found", ex);
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1){
            log.error("Exception while getting the bank details", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("End of BankController.findByName");
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        log.info("Inside BankController.deleteById, id: {}", id);
        try {
            bankService.deleteById(id);
            log.info("Bank with id {} deleted successfully", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BankDetailsNotFoundException ex) {
            log.error("Bank details not found for id {}", id, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex1) {
            log.error("Exception while deleting the bank", ex1);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping
    public ResponseEntity<BankTO> update(@RequestBody @NotNull BankUpdateRequest bankRequest){
        log.info("Inside the BankController.update, bankRequest:{}", bankRequest);
        BankTO bankTO = null;
        try{
            bankTO = bankService.update(bankRequest);
        } catch (BankDetailsNotFoundException ex){
            log.error("Bank details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex){
            log.error("Exception while getting bank details");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("End of BankController.update");
        return new ResponseEntity<>(bankTO, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<BankTO> save(@RequestBody BankRequest bankRequest){
        log.info("Inside the BankController.save, bankRequest:{}", bankRequest);
        BankTO bankTO = null;
        try{
            bankTO = bankService.save(bankRequest);
        } catch (BankDetailsNotFoundException ex){
            log.error("Bank details not found", ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex){
            log.error("Exception while getting bank details");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("End of BankController.save");
        return new ResponseEntity<>(bankTO, HttpStatus.OK);
    }
}
