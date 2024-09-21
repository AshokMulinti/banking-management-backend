package com.bankingmanagement.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "Loan")
public class Loan {
    @Id
   // @Field("id")
    private String id;
    @Field("loanId")
    private int loanId;
    @Field("loanType")
    private String loanType;
    @Field("loanAmount")
    private int loanAmount;
    @Field("branchId")
    private int branchId;
    @Field("custId")
    private int custId;
}
