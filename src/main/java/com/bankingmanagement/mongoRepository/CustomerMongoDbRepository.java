package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerMongoDbRepository extends MongoRepository<Customer,String> {
    Customer findByCustName(String custName);

    @Query("{custName : ?0}")
    Customer getCustomerByName(String custName);
}
