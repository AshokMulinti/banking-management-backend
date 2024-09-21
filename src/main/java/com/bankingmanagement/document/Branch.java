package com.bankingmanagement.document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection="Branch")
public class Branch {
    @Id
    //@Field("id")
    private String id;
    @Field("branchId")
    private int branchId;
    @Field("branchAddress")
    private String branchAddress;
    @Field("branchName")
    private String branchName;
    @Field("branchCode")
    private int branchCode;
}
