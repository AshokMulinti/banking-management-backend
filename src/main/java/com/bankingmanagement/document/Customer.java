package com.bankingmanagement.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "Customer")
public class Customer {
    @Id
   // @Field("id")
    private String id;
    @Field("custId")
    private int custId;
    @Field("custName")
    private String custName;
    @Field("custPhone")
    private int custPhone;
    @Field("custAddress")
    private String custAddress;
}
