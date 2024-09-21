package com.bankingmanagement.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "bank")
public class Bank {
    @Id
    //@Field("id")
    private String id;
    @Field("bankCode")
    private int bankCode;
    @Field("bankName")
    private String bankName;
    @Field("bankAddress")
    private String bankAddress;
}
