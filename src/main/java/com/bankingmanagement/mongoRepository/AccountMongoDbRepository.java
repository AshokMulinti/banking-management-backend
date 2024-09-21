package com.bankingmanagement.mongoRepository;


import com.bankingmanagement.document.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountMongoDbRepository extends MongoRepository<Account,String> {
}
