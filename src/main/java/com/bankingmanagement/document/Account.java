package com.bankingmanagement.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "Account")
public class Account {
    @Id
    //@Field("id")
    private String id;
    @Field("accountNo")
    private int accountNo;
    @Field("accountType")
    private String accountType;
    @Field("accountBalance")
    private int accountBalance;
}
