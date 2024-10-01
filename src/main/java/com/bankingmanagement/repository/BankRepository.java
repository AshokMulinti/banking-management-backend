package com.bankingmanagement.repository;

import com.bankingmanagement.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BankRepository extends JpaRepository<Bank,Integer> {

}
