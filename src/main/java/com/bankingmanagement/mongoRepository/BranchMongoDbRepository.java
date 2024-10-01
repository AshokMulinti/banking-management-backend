package com.bankingmanagement.mongoRepository;

import com.bankingmanagement.document.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface BranchMongoDbRepository extends MongoRepository<Branch,String> {
    Branch findByBranchName(String branchName);

    @Query("{branchName : ?0}")
    Branch getBranchByName(String branchName);
}
