package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankMongoDbRepository extends MongoRepository<Bank,String> {
}
