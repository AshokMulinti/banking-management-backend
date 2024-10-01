package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LoanMongoDbRepository extends MongoRepository<Loan,String> {
    Loan findByLoanType(String loanType);

//    @Query("{loanType : ?0}")
//    Loan getLoanByType(String loanType);
}
