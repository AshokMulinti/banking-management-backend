package com.bankingmanagement.mongoRepository;


import com.bankingmanagement.document.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AccountMongoDbRepository extends MongoRepository<Account,String> {
    Account findByAccountType(String accountType);

    @Query("{accountType : ?0}")
    Account getAccountByType(String accountType);
}
