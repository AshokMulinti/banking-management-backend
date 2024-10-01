package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BankMongoDbRepository extends MongoRepository<Bank,String> {
    Bank findByBankName(String name);

    @Query("{bankName : ?0}")
    Bank getBankByName(String name);
}
