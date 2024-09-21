package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoanMongoDbRepository extends MongoRepository<Loan,String> {
}
