package com.bankingmanagement.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BranchRequest {
    private int branchId;
    private String branchAddress;
    private String branchName;
    private int branchCode;
}
