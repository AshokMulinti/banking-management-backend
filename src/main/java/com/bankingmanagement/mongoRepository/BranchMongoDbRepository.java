package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BranchMongoDbRepository extends MongoRepository<Branch,String> {
}
