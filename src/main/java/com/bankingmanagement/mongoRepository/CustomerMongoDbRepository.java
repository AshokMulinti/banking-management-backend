package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerMongoDbRepository extends MongoRepository<Customer,String> {
}
